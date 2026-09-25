class Ayah {
  final int id;
  final int surahId;
  final int verseNumber;
  final String textArabic;
  final String textTranslation;
  final String textTafsir;
  final String? audioUrl;

  const Ayah({
    required this.id,
    required this.surahId,
    required this.verseNumber,
    required this.textArabic,
    this.textTranslation = '',
    this.textTafsir = '',
    this.audioUrl,
  });

  String get audioStreamUrl {
    final s = surahId.toString().padLeft(3, '0');
    final a = verseNumber.toString().padLeft(3, '0');
    return 'https://everyayah.com/data/Alafasy_128kbps/$s$a.mp3';
  }
}

class Reciter {
  final String id;
  final String nameArabic;
  final String nameEnglish;
  final String baseUrl;

  const Reciter({
    required this.id,
    required this.nameArabic,
    required this.nameEnglish,
    required this.baseUrl,
  });
}

const List<Reciter> availableReciters = [
  Reciter(
    id: 'alafasy',
    nameArabic: 'مشاري راشد العفاسي',
    nameEnglish: 'Mishary Rashid Alafasy',
    baseUrl: 'Alafasy_128kbps',
  ),
  Reciter(
    id: 'abdulbasit',
    nameArabic: 'عبد الباسط عبد الصمد (مرتل)',
    nameEnglish: 'Abdul Basit (Murattal)',
    baseUrl: 'Abdul_Basit_Murattal_192kbps',
  ),
  Reciter(
    id: 'husary',
    nameArabic: 'محمود خليل الحصري',
    nameEnglish: 'Mahmoud Khalil Al-Husary',
    baseUrl: 'Husary_128kbps',
  ),
  Reciter(
    id: 'ghamadi',
    nameArabic: 'سعد الغامدي',
    nameEnglish: 'Saad Al-Ghamdi',
    baseUrl: 'Ghamadi_40kbps',
  ),
];
