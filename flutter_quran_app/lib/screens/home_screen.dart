import 'package:flutter/material.dart';
import '../models/surah.dart';
import '../services/quran_service.dart';
import '../theme/app_theme.dart';
import 'surah_reader_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  String _searchQuery = '';

  @override
  Widget build(BuildContext context) {
    final filteredSurahs = QuranService.allSurahs.where((s) {
      if (_searchQuery.isEmpty) return true;
      final q = _searchQuery.toLowerCase();
      return s.nameArabic.contains(q) ||
          s.nameEnglish.toLowerCase().contains(q) ||
          s.id.toString() == q;
    }).toList();

    return Scaffold(
      body: CustomScrollView(
        slivers: [
          // Header
          SliverToBoxAdapter(
            child: Container(
              padding: const EdgeInsets.only(top: 50, bottom: 20, left: 16, right: 16),
              decoration: const BoxDecoration(
                gradient: LinearGradient(
                  colors: [AppColors.emeraldDark, AppColors.emeraldPrimary],
                  begin: Alignment.topCenter,
                  end: Alignment.bottomCenter,
                ),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text(
                            'IMANI | إِمَانِي',
                            style: TextStyle(
                              color: Colors.white,
                              fontSize: 26,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                          const SizedBox(height: 4),
                          Text(
                            'كِتَابٌ أَنزَلْنَاهُ إِلَيْكَ مُبَارَكٌ لِّيَدَّبَّرُوا آيَاتِهِ',
                            style: TextStyle(
                              color: AppColors.goldLight.withOpacity(0.9),
                              fontSize: 13,
                            ),
                          ),
                        ],
                      ),
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: AppColors.goldAccent.withOpacity(0.2),
                          shape: BoxShape.circle,
                        ),
                        child: const Icon(
                          Icons.menu_book_rounded,
                          color: AppColors.goldAccent,
                          size: 28,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),

          // Search Bar
          SliverToBoxAdapter(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: TextField(
                onChanged: (val) => setState(() => _searchQuery = val),
                decoration: InputDecoration(
                  hintText: 'ابحث عن سورة بالاسم أو الرقم...',
                  prefixIcon: const Icon(Icons.search, color: AppColors.emeraldPrimary),
                  filled: true,
                  fillColor: Theme.of(context).cardTheme.color,
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(16),
                    borderSide: BorderSide.none,
                  ),
                ),
              ),
            ),
          ),

          // Daily Ayah Inspiration Card
          if (_searchQuery.isEmpty)
            SliverToBoxAdapter(
              child: Container(
                margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: AppColors.emeraldContainer.withOpacity(0.5),
                  borderRadius: BorderRadius.circular(18),
                  border: Border.all(color: AppColors.goldAccent.withOpacity(0.3)),
                ),
                child: Column(
                  children: [
                    Row(
                      children: [
                        const Icon(Icons.auto_awesome, color: AppColors.goldAccent, size: 18),
                        const SizedBox(width: 8),
                        const Text(
                          'آية وتدبر',
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            color: AppColors.emeraldPrimary,
                          ),
                        ),
                        const Spacer(),
                        Text(
                          'سورة البقرة: ١٨٦',
                          style: TextStyle(fontSize: 11, color: Colors.grey.shade700),
                        ),
                      ],
                    ),
                    const SizedBox(height: 10),
                    const Text(
                      '﴿ وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ ۖ أُجِيبُ دَعْوَةَ الدَّاعِ إِذَا دَعَانِ ﴾',
                      style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                        height: 1.6,
                      ),
                      textAlign: TextAlign.center,
                    ),
                  ],
                ),
              ),
            ),

          // Section Title
          SliverToBoxAdapter(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 18, vertical: 12),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text(
                    'فهرس السور المباركة',
                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  Text(
                    '${filteredSurahs.length} سورة',
                    style: const TextStyle(color: AppColors.emeraldPrimary, fontSize: 13),
                  ),
                ],
              ),
            ),
          ),

          // Surah List
          SliverList(
            delegate: SliverChildBuilderDelegate(
              (context, index) {
                final surah = filteredSurahs[index];
                return Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 4),
                  child: Card(
                    child: ListTile(
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => SurahReaderScreen(surah: surah),
                          ),
                        );
                      },
                      leading: Container(
                        width: 42,
                        height: 42,
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          color: AppColors.emeraldContainer,
                          border: Border.all(color: AppColors.goldAccent.withOpacity(0.5)),
                        ),
                        alignment: Alignment.center,
                        child: Text(
                          surah.id.toString(),
                          style: const TextStyle(
                            fontWeight: FontWeight.bold,
                            color: AppColors.emeraldPrimary,
                          ),
                        ),
                      ),
                      title: Text(
                        'سورة ${surah.nameArabic}',
                        style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                      ),
                      subtitle: Text(
                        '${surah.nameEnglish} • ${surah.versesCount} آيات • الجزء ${surah.juz}',
                        style: TextStyle(color: Colors.grey.shade600, fontSize: 12),
                      ),
                      trailing: Container(
                        padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                        decoration: BoxDecoration(
                          color: surah.revelationType == RevelationType.meccan
                              ? const Color(0xFFFBE9E7)
                              : const Color(0xFFE8F5E9),
                          borderRadius: BorderRadius.circular(6),
                        ),
                        child: Text(
                          surah.revelationType.arabicLabel,
                          style: TextStyle(
                            fontSize: 10,
                            fontWeight: FontWeight.bold,
                            color: surah.revelationType == RevelationType.meccan
                                ? const Color(0xFFBF360C)
                                : const Color(0xFF1B5E20),
                          ),
                        ),
                      ),
                    ),
                  ),
                );
              },
              childCount: filteredSurahs.length,
            ),
          ),
          const SliverToBoxAdapter(child: SizedBox(height: 80)),
        ],
      ),
    );
  }
}
