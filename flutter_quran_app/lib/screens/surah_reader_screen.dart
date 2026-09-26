import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import '../models/surah.dart';
import '../models/ayah.dart';
import '../services/quran_service.dart';
import '../services/audio_player_service.dart';
import '../theme/app_theme.dart';

class SurahReaderScreen extends StatefulWidget {
  final Surah surah;

  const SurahReaderScreen({super.key, required this.surah});

  @override
  State<SurahReaderScreen> createState() => _SurahReaderScreenState();
}

class _SurahReaderScreenState extends State<SurahReaderScreen> {
  late Future<List<Ayah>> _versesFuture;
  double _fontSize = 24.0;
  bool _showTranslation = true;
  bool _showTafsir = false;

  @override
  void initState() {
    super.initState();
    _versesFuture = QuranService.getSurahVerses(widget.surah.id);
  }

  @override
  Widget build(BuildContext context) {
    final audio = context.watch<QuranAudioPlayerService>();

    return Container(
      color: Theme.of(context).scaffoldBackgroundColor,
      alignment: Alignment.center,
      child: ConstrainedBox(
        constraints: const BoxConstraints(maxWidth: 480),
        child: Scaffold(
      appBar: AppBar(
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'سُورَةُ ${widget.surah.nameArabic}',
              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 17),
            ),
            Text(
              '${widget.surah.nameEnglish} • ${widget.surah.versesCount} آيات',
              style: const TextStyle(fontSize: 11, color: AppColors.goldLight),
            ),
          ],
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.format_size),
            tooltip: 'تغيير حجم الخط',
            onPressed: () => _showFontSizeDialog(),
          ),
          IconButton(
            icon: Icon(
              Icons.translate,
              color: _showTranslation ? AppColors.goldAccent : Colors.white70,
            ),
            tooltip: 'الترجمة',
            onPressed: () => setState(() => _showTranslation = !_showTranslation),
          ),
          IconButton(
            icon: Icon(
              Icons.info_outline,
              color: _showTafsir ? AppColors.goldAccent : Colors.white70,
            ),
            tooltip: 'التفسير',
            onPressed: () => setState(() => _showTafsir = !_showTafsir),
          ),
        ],
      ),
      body: FutureBuilder<List<Ayah>>(
        future: _versesFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircularProgressIndicator(color: AppColors.emeraldPrimary),
                  SizedBox(height: 14),
                  Text('جارٍ تحميل الآيات الكريمة...'),
                ],
              ),
            );
          }

          if (snapshot.hasError || !snapshot.hasData || snapshot.data!.isEmpty) {
            return const Center(child: Text('تعذر تحميل الآيات. يرجى المحاولة لاحقاً.'));
          }

          final verses = snapshot.data!;

          return ListView.builder(
            padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 12),
            itemCount: verses.length + (widget.surah.id != 9 ? 1 : 0),
            itemBuilder: (context, index) {
              if (widget.surah.id != 9 && index == 0) {
                // Bismillah banner
                return Container(
                  margin: const EdgeInsets.symmetric(vertical: 14),
                  padding: const EdgeInsets.all(14),
                  decoration: BoxDecoration(
                    color: AppColors.emeraldContainer.withOpacity(0.5),
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(color: AppColors.goldAccent.withOpacity(0.4)),
                  ),
                  child: const Text(
                    'بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ',
                    style: TextStyle(
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                      color: AppColors.emeraldPrimary,
                    ),
                    textAlign: TextAlign.center,
                  ),
                );
              }

              final ayahIndex = widget.surah.id != 9 ? index - 1 : index;
              final ayah = verses[ayahIndex];
              final isCurrentAyahPlaying = audio.isPlaying &&
                  audio.activeSurahId == widget.surah.id &&
                  audio.activeAyahNumber == ayah.verseNumber;

              return Card(
                color: isCurrentAyahPlaying
                    ? AppColors.emeraldContainer.withOpacity(0.4)
                    : Theme.of(context).cardTheme.color,
                margin: const EdgeInsets.symmetric(vertical: 6),
                child: Padding(
                  padding: const EdgeInsets.all(14),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      // Ayah text and number
                      Text(
                        '${ayah.textArabic} ﴿${ayah.verseNumber}﴾',
                        style: TextStyle(
                          fontSize: _fontSize,
                          fontWeight: FontWeight.bold,
                          height: 1.8,
                        ),
                        textAlign: TextAlign.right,
                      ),

                      if (_showTranslation && ayah.textTranslation.isNotEmpty) ...[
                        const Divider(height: 18),
                        Text(
                          ayah.textTranslation,
                          style: TextStyle(fontSize: 13, color: Colors.grey.shade700),
                        ),
                      ],

                      if (_showTafsir && ayah.textTafsir.isNotEmpty) ...[
                        const SizedBox(height: 8),
                        Container(
                          padding: const EdgeInsets.all(10),
                          decoration: BoxDecoration(
                            color: Colors.black.withOpacity(0.04),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Text(
                            ayah.textTafsir,
                            style: const TextStyle(fontSize: 12),
                          ),
                        ),
                      ],

                      const SizedBox(height: 8),
                      // Controls for this Ayah
                      Row(
                        mainAxisAlignment: MainAxisAlignment.end,
                        children: [
                          IconButton(
                            icon: Icon(
                              isCurrentAyahPlaying ? Icons.pause_circle : Icons.play_circle,
                              color: isCurrentAyahPlaying ? AppColors.emeraldPrimary : Colors.grey,
                            ),
                            tooltip: 'استماع للآية',
                            onPressed: () {
                              if (isCurrentAyahPlaying) {
                                audio.pause();
                              } else {
                                audio.playAyahAudio(
                                  surahId: widget.surah.id,
                                  ayahNumber: ayah.verseNumber,
                                  surahName: widget.surah.nameArabic,
                                );
                              }
                            },
                          ),
                          IconButton(
                            icon: const Icon(Icons.copy, size: 18, color: Colors.grey),
                            tooltip: 'نسخ الآية',
                            onPressed: () {
                              Clipboard.setData(
                                ClipboardData(
                                  text: '${ayah.textArabic} [سورة ${widget.surah.nameArabic}: ${ayah.verseNumber}]',
                                ),
                              );
                              ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(content: Text('تم نسخ الآية الكريمة')),
                              );
                            },
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              );
            },
          );
        },
      ),
    ),
    ),
    );
  }

  void _showFontSizeDialog() {
    showDialog(
      context: context,
      builder: (context) {
        return StatefulBuilder(
          builder: (context, setDialogState) {
            return AlertDialog(
              title: const Text('حجم الخط'),
              content: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(
                    'بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ',
                    style: TextStyle(fontSize: _fontSize, fontWeight: FontWeight.bold),
                    textAlign: TextAlign.center,
                  ),
                  Slider(
                    value: _fontSize,
                    min: 18.0,
                    max: 38.0,
                    divisions: 10,
                    label: '${_fontSize.toInt()} px',
                    onChanged: (val) {
                      setDialogState(() => _fontSize = val);
                      setState(() => _fontSize = val);
                    },
                  ),
                ],
              ),
              actions: [
                TextButton(
                  onPressed: () => Navigator.pop(context),
                  child: const Text('تم'),
                ),
              ],
            );
          },
        );
      },
    );
  }
}
