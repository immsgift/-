import 'package:flutter/material.dart';
import '../models/story.dart';
import '../services/stories_data.dart';
import '../theme/app_theme.dart';
import 'story_detail_screen.dart';

class StoriesScreen extends StatefulWidget {
  const StoriesScreen({super.key});

  @override
  State<StoriesScreen> createState() => _StoriesScreenState();
}

class _StoriesScreenState extends State<StoriesScreen> with SingleTickerProviderStateMixin {
  late TabController _tabController;
  String _searchQuery = '';

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final prophetsList = StoriesData.allStories
        .where((s) => s.category == StoryCategory.prophet)
        .where((s) {
      if (_searchQuery.isEmpty) return true;
      return s.title.contains(_searchQuery) || s.subtitle.contains(_searchQuery);
    }).toList();

    final sahabaList = StoriesData.allStories
        .where((s) => s.category == StoryCategory.sahabi)
        .where((s) {
      if (_searchQuery.isEmpty) return true;
      return s.title.contains(_searchQuery) || s.subtitle.contains(_searchQuery);
    }).toList();

    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'قصص الأنبياء والصحابة',
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
        ),
        centerTitle: true,
        bottom: TabBar(
          controller: _tabController,
          indicatorColor: AppColors.goldAccent,
          indicatorWeight: 3,
          labelColor: Colors.white,
          unselectedLabelColor: Colors.white70,
          labelStyle: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
          tabs: const [
            Tab(
              icon: Icon(Icons.auto_stories),
              text: 'قصص الأنبياء',
            ),
            Tab(
              icon: Icon(Icons.people_alt_outlined),
              text: 'سير الصحابة',
            ),
          ],
        ),
      ),
      body: Column(
        children: [
          // Search input
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 12, 16, 8),
            child: TextField(
              onChanged: (val) => setState(() => _searchQuery = val),
              decoration: InputDecoration(
                hintText: 'ابحث عن نبي أو صحابي جليل...',
                prefixIcon: const Icon(Icons.search, color: AppColors.emeraldPrimary),
                filled: true,
                fillColor: Theme.of(context).cardTheme.color,
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(16),
                  borderSide: BorderSide.none,
                ),
                contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
              ),
            ),
          ),

          Expanded(
            child: TabBarView(
              controller: _tabController,
              children: [
                _buildStoriesList(prophetsList, isProphets: true),
                _buildStoriesList(sahabaList, isProphets: false),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildStoriesList(List<IslamicStory> stories, {required bool isProphets}) {
    if (stories.isEmpty) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.search_off, size: 54, color: Colors.grey.shade400),
            const SizedBox(height: 12),
            const Text(
              'لا توجد نتائج مطابقة لبحثك',
              style: TextStyle(fontSize: 15, color: Colors.grey),
            ),
          ],
        ),
      );
    }

    return ListView.builder(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      itemCount: stories.length + 1,
      itemBuilder: (context, index) {
        if (index == stories.length) {
          return const SizedBox(height: 80);
        }

        final story = stories[index];
        return Card(
          margin: const EdgeInsets.only(bottom: 12),
          elevation: 1.5,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          child: InkWell(
            borderRadius: BorderRadius.circular(16),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => StoryDetailScreen(story: story),
                ),
              );
            },
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Row(
                children: [
                  Container(
                    width: 50,
                    height: 50,
                    decoration: BoxDecoration(
                      color: isProphets
                          ? AppColors.emeraldContainer
                          : AppColors.goldAccent.withOpacity(0.15),
                      shape: BoxShape.circle,
                      border: Border.all(
                        color: isProphets ? AppColors.emeraldPrimary : AppColors.goldAccent,
                        width: 1.2,
                      ),
                    ),
                    child: Icon(
                      isProphets ? Icons.stars_rounded : Icons.person_rounded,
                      color: isProphets ? AppColors.emeraldPrimary : AppColors.goldAccent,
                      size: 26,
                    ),
                  ),
                  const SizedBox(width: 14),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          children: [
                            Expanded(
                              child: Text(
                                story.title,
                                style: const TextStyle(
                                  fontWeight: FontWeight.bold,
                                  fontSize: 16,
                                ),
                              ),
                            ),
                            Container(
                              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                              decoration: BoxDecoration(
                                color: isProphets
                                    ? AppColors.emeraldPrimary.withOpacity(0.1)
                                    : AppColors.goldAccent.withOpacity(0.15),
                                borderRadius: BorderRadius.circular(8),
                              ),
                              child: Text(
                                story.eraOrTitle,
                                style: TextStyle(
                                  fontSize: 10,
                                  fontWeight: FontWeight.bold,
                                  color: isProphets ? AppColors.emeraldPrimary : AppColors.goldAccent,
                                ),
                              ),
                            ),
                          ],
                        ),
                        const SizedBox(height: 6),
                        Text(
                          story.subtitle,
                          style: TextStyle(
                            fontSize: 12,
                            color: Colors.grey.shade600,
                            height: 1.3,
                          ),
                          maxLines: 2,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(width: 8),
                  const Icon(
                    Icons.arrow_forward_ios_rounded,
                    size: 16,
                    color: Colors.grey,
                  ),
                ],
              ),
            ),
          ),
        );
      },
    );
  }
}
