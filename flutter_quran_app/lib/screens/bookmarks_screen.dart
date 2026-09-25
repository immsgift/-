import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

class BookmarksScreen extends StatelessWidget {
  const BookmarksScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 2,
      child: Scaffold(
        appBar: AppBar(
          title: const Text('المحفوظات والختمات', style: TextStyle(fontWeight: FontWeight.bold)),
          bottom: const TabBar(
            indicatorColor: AppColors.goldAccent,
            labelColor: Colors.white,
            unselectedLabelColor: Colors.white70,
            tabs: [
              Tab(text: 'الفواصل والمحفوظات'),
              Tab(text: 'ختمة القرآن'),
            ],
          ),
        ),
        body: TabBarView(
          children: [
            // Bookmarks list
            ListView(
              padding: const EdgeInsets.all(16),
              children: [
                Card(
                  child: ListTile(
                    leading: const CircleAvatar(
                      backgroundColor: AppColors.emeraldContainer,
                      child: Icon(Icons.bookmark, color: AppColors.emeraldPrimary),
                    ),
                    title: const Text('سورة الكهف • الآية ١٠', style: TextStyle(fontWeight: FontWeight.bold)),
                    subtitle: const Text('﴿ إِذْ أَوَى الْفِتْيَةُ إِلَى الْكَهْفِ فَقَالُوا رَبَّنَا آتِنَا مِن لَّدُنكَ رَحْمَةً... ﴾'),
                    trailing: IconButton(
                      icon: const Icon(Icons.delete_outline, color: Colors.redAccent),
                      onPressed: () {},
                    ),
                  ),
                ),
              ],
            ),

            // Khatmah progress tracker
            Padding(
              padding: const EdgeInsets.all(20),
              child: Column(
                children: [
                  Card(
                    child: Padding(
                      padding: const EdgeInsets.all(16),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text(
                            'خطة الختمة الشهرية',
                            style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.emeraldPrimary),
                          ),
                          const SizedBox(height: 6),
                          const Text(
                            'قراءة جزء واحد يومياً (حوالي 20 صفحة، 4 صفحات بعد كل صلاة) لإتمام الختمة في 30 يوماً.',
                            style: TextStyle(fontSize: 13, height: 1.5),
                          ),
                          const SizedBox(height: 16),
                          LinearProgressIndicator(
                            value: 0.35,
                            backgroundColor: Colors.grey.shade200,
                            color: AppColors.goldAccent,
                            minHeight: 8,
                            borderRadius: BorderRadius.circular(4),
                          ),
                          const SizedBox(height: 8),
                          const Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              Text('الجزء الحالي: ١١ من ٣٠', style: TextStyle(fontSize: 12, color: Colors.grey)),
                              Text('٣٥٪ مكتمل', style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold, color: AppColors.emeraldPrimary)),
                            ],
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
