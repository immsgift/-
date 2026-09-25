import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import '../models/adhkar.dart';
import '../services/quran_service.dart';
import '../services/audio_player_service.dart';
import '../theme/app_theme.dart';

class AdhkarScreen extends StatefulWidget {
  const AdhkarScreen({super.key});

  @override
  State<AdhkarScreen> createState() => _AdhkarScreenState();
}

class _AdhkarScreenState extends State<AdhkarScreen> {
  String _selectedCategoryId = 'morning';
  final Map<String, int> _counts = {};
  String _searchQuery = '';

  @override
  Widget build(BuildContext context) {
    final audio = context.watch<QuranAudioPlayerService>();

    final displayedItems = QuranService.allAdhkar.where((item) {
      if (_searchQuery.isNotEmpty) {
        final q = _searchQuery.toLowerCase();
        return item.title.toLowerCase().contains(q) ||
            item.arabicText.contains(q) ||
            item.virtue.contains(q);
      }
      return item.categoryId == _selectedCategoryId;
    }).toList();

    return Scaffold(
      appBar: AppBar(
        title: const Text('الأذكار والأدعية', style: TextStyle(fontWeight: FontWeight.bold)),
      ),
      body: Column(
        children: [
          // Category chips
          Container(
            height: 52,
            margin: const EdgeInsets.symmetric(vertical: 8),
            child: ListView.builder(
              scrollDirection: Axis.horizontal,
              padding: const EdgeInsets.symmetric(horizontal: 14),
              itemCount: QuranService.adhkarCategories.length,
              itemBuilder: (context, index) {
                final cat = QuranService.adhkarCategories[index];
                final isSelected = cat.id == _selectedCategoryId && _searchQuery.isEmpty;
                return Padding(
                  padding: const EdgeInsets.only(right: 8),
                  child: FilterChip(
                    selected: isSelected,
                    label: Text(cat.titleArabic),
                    selectedColor: AppColors.emeraldPrimary,
                    labelStyle: TextStyle(
                      color: isSelected ? Colors.white : Colors.black87,
                      fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
                    ),
                    onSelected: (_) {
                      setState(() {
                        _searchQuery = '';
                        _selectedCategoryId = cat.id;
                      });
                    },
                  ),
                );
              },
            ),
          ),

          // Search bar
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
            child: TextField(
              onChanged: (val) => setState(() => _searchQuery = val),
              decoration: InputDecoration(
                hintText: 'ابحث في الأدعية والأذكار...',
                prefixIcon: const Icon(Icons.search, size: 20),
                contentPadding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
                filled: true,
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(14),
                  borderSide: BorderSide.none,
                ),
              ),
            ),
          ),

          // Items list
          Expanded(
            child: ListView.builder(
              padding: const EdgeInsets.all(14),
              itemCount: displayedItems.length,
              itemBuilder: (context, index) {
                final item = displayedItems[index];
                final count = _counts[item.id] ?? 0;
                final isCompleted = count >= item.targetCount;

                return Card(
                  color: isCompleted
                      ? const Color(0xFFF1F8F4)
                      : Theme.of(context).cardTheme.color,
                  margin: const EdgeInsets.only(bottom: 12),
                  child: Padding(
                    padding: const EdgeInsets.all(16),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              item.title,
                              style: const TextStyle(
                                fontWeight: FontWeight.bold,
                                fontSize: 16,
                                color: AppColors.emeraldPrimary,
                              ),
                            ),
                            IconButton(
                              icon: const Icon(Icons.volume_up, color: AppColors.emeraldPrimary),
                              tooltip: 'استماع للذكر',
                              onPressed: () {
                                audio.playCustomAudio(
                                  url: 'https://everyayah.com/data/Alafasy_128kbps/001001.mp3',
                                  title: item.title,
                                  subtitle: item.reference,
                                );
                              },
                            ),
                          ],
                        ),
                        const SizedBox(height: 8),
                        Text(
                          item.arabicText,
                          style: const TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                            height: 1.8,
                          ),
                          textAlign: TextAlign.right,
                        ),
                        if (item.virtue.isNotEmpty) ...[
                          const SizedBox(height: 10),
                          Container(
                            padding: const EdgeInsets.all(8),
                            decoration: BoxDecoration(
                              color: AppColors.goldLight.withOpacity(0.3),
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Text(
                              'الفضل: ${item.virtue}',
                              style: const TextStyle(fontSize: 12, color: Color(0xFF7E5C00)),
                            ),
                          ),
                        ],
                        const SizedBox(height: 14),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            IconButton(
                              icon: const Icon(Icons.copy, size: 18, color: Colors.grey),
                              onPressed: () {
                                Clipboard.setData(
                                  ClipboardData(
                                    text: '${item.arabicText}\n[${item.title} - ${item.reference}]',
                                  ),
                                );
                                ScaffoldMessenger.of(context).showSnackBar(
                                  const SnackBar(content: Text('تم نسخ الذكر')),
                                );
                              },
                            ),
                            ElevatedButton.icon(
                              style: ElevatedButton.styleFrom(
                                backgroundColor: isCompleted
                                    ? AppColors.emeraldPrimary
                                    : AppColors.emeraldDark,
                                foregroundColor: Colors.white,
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(14),
                                ),
                              ),
                              onPressed: () {
                                setState(() {
                                  if (count < item.targetCount) {
                                    _counts[item.id] = count + 1;
                                    HapticFeedback.lightImpact();
                                  }
                                });
                              },
                              icon: Icon(isCompleted ? Icons.check : Icons.touch_app, size: 18),
                              label: Text(
                                isCompleted
                                    ? 'اكتمل (${item.targetCount})'
                                    : 'قراءة: $count / ${item.targetCount}',
                                style: const TextStyle(fontWeight: FontWeight.bold),
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
