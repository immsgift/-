class DhikrCategory {
  final String id;
  final String titleArabic;
  final String titleEnglish;
  final String iconName;
  final bool isOccasion;
  final String countHint;

  const DhikrCategory({
    required this.id,
    required this.titleArabic,
    required this.titleEnglish,
    required this.iconName,
    this.isOccasion = false,
    this.countHint = '',
  });
}

class DhikrItem {
  final String id;
  final String categoryId;
  final String title;
  final String arabicText;
  final String translation;
  final String reference;
  final String virtue;
  final int targetCount;

  const DhikrItem({
    required this.id,
    required this.categoryId,
    required this.title,
    required this.arabicText,
    this.translation = '',
    this.reference = '',
    this.virtue = '',
    this.targetCount = 1,
  });
}
