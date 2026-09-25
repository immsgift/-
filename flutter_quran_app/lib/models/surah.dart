enum RevelationType {
  meccan,
  medinan,
}

extension RevelationTypeExtension on RevelationType {
  String get arabicLabel => this == RevelationType.meccan ? 'مكية 🕋' : 'مدنية 🕌';
}

class Surah {
  final int id;
  final String nameArabic;
  final String nameEnglish;
  final String englishMeaning;
  final int versesCount;
  final RevelationType revelationType;
  final int juz;
  final int pageNumber;

  const Surah({
    required this.id,
    required this.nameArabic,
    required this.nameEnglish,
    required this.englishMeaning,
    required this.versesCount,
    required this.revelationType,
    required this.juz,
    required this.pageNumber,
  });

  factory Surah.fromJson(Map<String, dynamic> json) {
    return Surah(
      id: json['number'] as int,
      nameArabic: json['name'] as String,
      nameEnglish: json['englishName'] as String,
      englishMeaning: json['englishNameTranslation'] as String? ?? '',
      versesCount: json['numberOfAyahs'] as int? ?? 0,
      revelationType: (json['revelationType'] as String? ?? 'Meccan').toLowerCase() == 'meccan'
          ? RevelationType.meccan
          : RevelationType.medinan,
      juz: json['juz'] as int? ?? 1,
      pageNumber: json['page'] as int? ?? 1,
    );
  }
}
