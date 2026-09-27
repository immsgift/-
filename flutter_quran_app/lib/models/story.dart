enum StoryCategory { prophet, sahabi }

extension StoryCategoryExt on StoryCategory {
  String get label {
    switch (this) {
      case StoryCategory.prophet:
        return 'قصص الأنبياء';
      case StoryCategory.sahabi:
        return 'سير الصحابة';
    }
  }
}

class IslamicStory {
  final String id;
  final String title;
  final String subtitle;
  final StoryCategory category;
  final String eraOrTitle;
  final String quranicAyah;
  final String ayahReference;
  final List<String> paragraphs;
  final List<String> lessonsLearned;

  const IslamicStory({
    required this.id,
    required this.title,
    required this.subtitle,
    required this.category,
    required this.eraOrTitle,
    required this.quranicAyah,
    required this.ayahReference,
    required this.paragraphs,
    required this.lessonsLearned,
  });
}
