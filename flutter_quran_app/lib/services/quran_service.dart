import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/surah.dart';
import '../models/ayah.dart';
import '../models/adhkar.dart';

class QuranService {
  static const List<Surah> allSurahs = [
    Surah(id: 1, nameArabic: 'الفاتحة', nameEnglish: 'Al-Fatihah', englishMeaning: 'The Opener', versesCount: 7, revelationType: RevelationType.meccan, juz: 1, pageNumber: 1),
    Surah(id: 2, nameArabic: 'البقرة', nameEnglish: 'Al-Baqarah', englishMeaning: 'The Cow', versesCount: 286, revelationType: RevelationType.medinan, juz: 1, pageNumber: 2),
    Surah(id: 3, nameArabic: 'آل عمران', nameEnglish: 'Ali \'Imran', englishMeaning: 'Family of Imran', versesCount: 200, revelationType: RevelationType.medinan, juz: 3, pageNumber: 50),
    Surah(id: 4, nameArabic: 'النساء', nameEnglish: 'An-Nisa', englishMeaning: 'The Women', versesCount: 176, revelationType: RevelationType.medinan, juz: 4, pageNumber: 77),
    Surah(id: 5, nameArabic: 'المائدة', nameEnglish: 'Al-Ma\'idah', englishMeaning: 'The Table Spread', versesCount: 120, revelationType: RevelationType.medinan, juz: 6, pageNumber: 106),
    Surah(id: 6, nameArabic: 'الأنعام', nameEnglish: 'Al-An\'am', englishMeaning: 'The Cattle', versesCount: 165, revelationType: RevelationType.meccan, juz: 7, pageNumber: 128),
    Surah(id: 7, nameArabic: 'الأعراف', nameEnglish: 'Al-A\'raf', englishMeaning: 'The Heights', versesCount: 206, revelationType: RevelationType.meccan, juz: 8, pageNumber: 151),
    Surah(id: 8, nameArabic: 'الأنفال', nameEnglish: 'Al-Anfal', englishMeaning: 'The Spoils of War', versesCount: 75, revelationType: RevelationType.medinan, juz: 9, pageNumber: 177),
    Surah(id: 9, nameArabic: 'التوبة', nameEnglish: 'At-Tawbah', englishMeaning: 'The Repentance', versesCount: 129, revelationType: RevelationType.medinan, juz: 10, pageNumber: 187),
    Surah(id: 10, nameArabic: 'يونس', nameEnglish: 'Yunus', englishMeaning: 'Jonah', versesCount: 109, revelationType: RevelationType.meccan, juz: 11, pageNumber: 208),
    Surah(id: 11, nameArabic: 'هود', nameEnglish: 'Hud', englishMeaning: 'Hud', versesCount: 123, revelationType: RevelationType.meccan, juz: 11, pageNumber: 221),
    Surah(id: 12, nameArabic: 'يوسف', nameEnglish: 'Yusuf', englishMeaning: 'Joseph', versesCount: 111, revelationType: RevelationType.meccan, juz: 12, pageNumber: 235),
    Surah(id: 13, nameArabic: 'الرعد', nameEnglish: 'Ar-Ra\'d', englishMeaning: 'The Thunder', versesCount: 43, revelationType: RevelationType.medinan, juz: 13, pageNumber: 249),
    Surah(id: 14, nameArabic: 'إبراهيم', nameEnglish: 'Ibrahim', englishMeaning: 'Abraham', versesCount: 52, revelationType: RevelationType.meccan, juz: 13, pageNumber: 255),
    Surah(id: 15, nameArabic: 'الحجر', nameEnglish: 'Al-Hijr', englishMeaning: 'The Rocky Tract', versesCount: 99, revelationType: RevelationType.meccan, juz: 14, pageNumber: 262),
    Surah(id: 16, nameArabic: 'النحل', nameEnglish: 'An-Nahl', englishMeaning: 'The Bee', versesCount: 128, revelationType: RevelationType.meccan, juz: 14, pageNumber: 267),
    Surah(id: 17, nameArabic: 'الإسراء', nameEnglish: 'Al-Isra', englishMeaning: 'The Night Journey', versesCount: 111, revelationType: RevelationType.meccan, juz: 15, pageNumber: 282),
    Surah(id: 18, nameArabic: 'الكهف', nameEnglish: 'Al-Kahf', englishMeaning: 'The Cave', versesCount: 110, revelationType: RevelationType.meccan, juz: 15, pageNumber: 293),
    Surah(id: 19, nameArabic: 'مريم', nameEnglish: 'Maryam', englishMeaning: 'Mary', versesCount: 98, revelationType: RevelationType.meccan, juz: 16, pageNumber: 305),
    Surah(id: 20, nameArabic: 'طه', nameEnglish: 'Ta-Ha', englishMeaning: 'Ta-Ha', versesCount: 135, revelationType: RevelationType.meccan, juz: 16, pageNumber: 312),
    Surah(id: 36, nameArabic: 'يس', nameEnglish: 'Ya-Sin', englishMeaning: 'Ya-Sin', versesCount: 83, revelationType: RevelationType.meccan, juz: 22, pageNumber: 440),
    Surah(id: 55, nameArabic: 'الرحمن', nameEnglish: 'Ar-Rahman', englishMeaning: 'The Beneficent', versesCount: 78, revelationType: RevelationType.medinan, juz: 27, pageNumber: 531),
    Surah(id: 56, nameArabic: 'الواقعة', nameEnglish: 'Al-Waqi\'ah', englishMeaning: 'The Inevitable', versesCount: 96, revelationType: RevelationType.meccan, juz: 27, pageNumber: 534),
    Surah(id: 67, nameArabic: 'الملك', nameEnglish: 'Al-Mulk', englishMeaning: 'The Sovereignty', versesCount: 30, revelationType: RevelationType.meccan, juz: 29, pageNumber: 562),
    Surah(id: 97, nameArabic: 'القدر', nameEnglish: 'Al-Qadr', englishMeaning: 'The Power', versesCount: 5, revelationType: RevelationType.meccan, juz: 30, pageNumber: 598),
    Surah(id: 103, nameArabic: 'العصر', nameEnglish: 'Al-\'Asr', englishMeaning: 'The Declining Day', versesCount: 3, revelationType: RevelationType.meccan, juz: 30, pageNumber: 601),
    Surah(id: 108, nameArabic: 'الكوثر', nameEnglish: 'Al-Kawthar', englishMeaning: 'The Abundance', versesCount: 3, revelationType: RevelationType.meccan, juz: 30, pageNumber: 602),
    Surah(id: 112, nameArabic: 'الإخلاص', nameEnglish: 'Al-Ikhlas', englishMeaning: 'The Sincerity', versesCount: 4, revelationType: RevelationType.meccan, juz: 30, pageNumber: 604),
    Surah(id: 113, nameArabic: 'الفلق', nameEnglish: 'Al-Falaq', englishMeaning: 'The Daybreak', versesCount: 5, revelationType: RevelationType.meccan, juz: 30, pageNumber: 604),
    Surah(id: 114, nameArabic: 'الناس', nameEnglish: 'An-Nas', englishMeaning: 'Mankind', versesCount: 6, revelationType: RevelationType.meccan, juz: 30, pageNumber: 604),
  ];

  static final Map<int, List<Ayah>> _cachedVerses = {
    1: [
      Ayah(id: 1, surahId: 1, verseNumber: 1, textArabic: 'بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ', textTranslation: 'In the name of Allah, the Entirely Merciful, the Especially Merciful.', textTafsir: 'ابتدأ بالبسملة استعانة بالله تعالى وتبركاً باسمه.'),
      Ayah(id: 2, surahId: 1, verseNumber: 2, textArabic: 'الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ', textTranslation: '[All] praise is [due] to Allah, Lord of the worlds -', textTafsir: 'الثناء الكامل والشكر الخالص لله تعالى خالق كل شيء.'),
      Ayah(id: 3, surahId: 1, verseNumber: 3, textArabic: 'الرَّحْمَٰنِ الرَّحِيمِ', textTranslation: 'The Entirely Merciful, the Especially Merciful,', textTafsir: 'الرحمن ذو الرحمة الشاملة لجميع الخلائق.'),
      Ayah(id: 4, surahId: 1, verseNumber: 4, textArabic: 'مَالِكِ يَوْمِ الدِّينِ', textTranslation: 'Sovereign of the Day of Recompense.', textTafsir: 'المتصرف وحده في يوم الجزاء والحساب.'),
      Ayah(id: 5, surahId: 1, verseNumber: 5, textArabic: 'إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ', textTranslation: 'It is You we worship and You we ask for help.', textTafsir: 'نخصك وحدك بالعبادة ونتوجه إليك وحدك بالاستعانة.'),
      Ayah(id: 6, surahId: 1, verseNumber: 6, textArabic: 'اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ', textTranslation: 'Guide us to the straight path -', textTafsir: 'وفقنا وسددنا للثبات على طريق الحق وهو الإسلام.'),
      Ayah(id: 7, surahId: 1, verseNumber: 7, textArabic: 'صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ', textTranslation: 'The path of those upon whom You have bestowed favor, not of those who have evoked anger or are astray.', textTafsir: 'طريق الأنبياء والصديقين والشهداء والصالحين.'),
    ],
    112: [
      Ayah(id: 1, surahId: 112, verseNumber: 1, textArabic: 'قُلْ هُوَ اللَّهُ أَحَدٌ', textTranslation: 'Say, "He is Allah, [who is] One,', textTafsir: 'قل يا محمد: هو الله المتفرد بالألوهية لا شريك له.'),
      Ayah(id: 2, surahId: 112, verseNumber: 2, textArabic: 'اللَّهُ الصَّمَدُ', textTranslation: 'Allah, the Eternal Refuge.', textTafsir: 'السيد الذي تصمد إليه الخلائق في حوائجها.'),
      Ayah(id: 3, surahId: 112, verseNumber: 3, textArabic: 'لَمْ يَلِدْ وَلَمْ يُولَدْ', textTranslation: 'He neither begets nor is born,', textTafsir: 'ليس له ولد ولا والد تنزه سبحانه.'),
      Ayah(id: 4, surahId: 112, verseNumber: 4, textArabic: 'وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ', textTranslation: 'Nor is there to Him any equivalent."', textTafsir: 'وليس له مماثل ولا نظير في صفاته وأفعاله.'),
    ],
    113: [
      Ayah(id: 1, surahId: 113, verseNumber: 1, textArabic: 'قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ', textTranslation: 'Say, "I seek refuge in the Lord of daybreak', textTafsir: 'أعتصم وألتجئ برب الصبح وفالقه.'),
      Ayah(id: 2, surahId: 113, verseNumber: 2, textArabic: 'مِن شَرِّ مَا خَلَقَ', textTranslation: 'From the evil of that which He created', textTafsir: 'من شر جميع المخلوقات وأذاها.'),
      Ayah(id: 3, surahId: 113, verseNumber: 3, textArabic: 'وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ', textTranslation: 'And from the evil of darkness when it settles', textTafsir: 'ومن شر الليل إذا أقبل بظلامه.'),
      Ayah(id: 4, surahId: 113, verseNumber: 4, textArabic: 'وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ', textTranslation: 'And from the evil of the blowers in knots', textTafsir: 'ومن شر السواحر اللاتي يعقدن العقد وينفثن فيها.'),
      Ayah(id: 5, surahId: 113, verseNumber: 5, textArabic: 'وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ', textTranslation: 'And from the evil of an envier when he envies."', textTafsir: 'ومن شر الحاسد إذا أظهر حسده وتمنى زوال النعمة.'),
    ],
    114: [
      Ayah(id: 1, surahId: 114, verseNumber: 1, textArabic: 'قُلْ أَعُوذُ بِرَبِّ النَّاسِ', textTranslation: 'Say, "I seek refuge in the Lord of mankind,', textTafsir: 'أعتصم برب الناس وخالقهم ومدبر أمورهم.'),
      Ayah(id: 2, surahId: 114, verseNumber: 2, textArabic: 'مَلِكِ النَّاسِ', textTranslation: 'The Sovereign of mankind,', textTafsir: 'الملك الحق المتصرف في شؤونهم.'),
      Ayah(id: 3, surahId: 114, verseNumber: 3, textArabic: 'إِلَٰهِ النَّاسِ', textTranslation: 'The God of mankind,', textTafsir: 'معبودهم الحق الذي لا إله غيره.'),
      Ayah(id: 4, surahId: 114, verseNumber: 4, textArabic: 'مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ', textTranslation: 'From the evil of the retreating whisperer -', textTafsir: 'من شر الشيطان الذي يوسوس عند الغفلة ويختفي عند الذكر.'),
      Ayah(id: 5, surahId: 114, verseNumber: 5, textArabic: 'الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ', textTranslation: 'Who whispers into the breasts of mankind -', textTafsir: 'الذي يبث الشكوك والشرور في صدور الناس.'),
      Ayah(id: 6, surahId: 114, verseNumber: 6, textArabic: 'مِنَ الْجِنَّةِ وَالنَّاسِ', textTranslation: 'From among the jinn and mankind."', textTafsir: 'من شياطين الإنس والجن.'),
    ]
  };

  static Future<List<Ayah>> getSurahVerses(int surahId) async {
    if (_cachedVerses.containsKey(surahId)) {
      return _cachedVerses[surahId]!;
    }

    try {
      final res = await http.get(
        Uri.parse('https://api.alquran.cloud/v1/surah/$surahId/editions/quran-uthmani,en.sahih'),
      ).timeout(const Duration(seconds: 8));

      if (res.statusCode == 200) {
        final body = json.decode(res.body);
        final data = body['data'] as List;
        final arabicList = data[0]['ayahs'] as List;
        final englishList = data.length > 1 ? data[1]['ayahs'] as List : [];

        final List<Ayah> ayahs = [];
        for (int i = 0; i < arabicList.length; i++) {
          final item = arabicList[i];
          final eng = i < englishList.length ? englishList[i]['text'] as String : '';
          ayahs.add(
            Ayah(
              id: item['number'] as int,
              surahId: surahId,
              verseNumber: item['numberInSurah'] as int,
              textArabic: item['text'] as String,
              textTranslation: eng,
              textTafsir: '',
            ),
          );
        }
        _cachedVerses[surahId] = ayahs;
        return ayahs;
      }
    } catch (_) {}

    // Fallback if offline
    return List.generate(
      10,
      (i) => Ayah(
        id: surahId * 1000 + i + 1,
        surahId: surahId,
        verseNumber: i + 1,
        textArabic: 'بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ آية كريمة رقم ${i + 1}',
        textTranslation: 'Verse ${i + 1} of Surah $surahId',
      ),
    );
  }

  // Categories of Adhkar
  static const List<DhikrCategory> adhkarCategories = [
    DhikrCategory(id: 'morning', titleArabic: 'أذكار الصباح', titleEnglish: 'Morning Adhkar', iconName: 'wb_sunny', countHint: 'بعد صلاة الفجر'),
    DhikrCategory(id: 'evening', titleArabic: 'أذكار المساء', titleEnglish: 'Evening Adhkar', iconName: 'nights_stay', countHint: 'بعد صلاة العصر'),
    DhikrCategory(id: 'sleep', titleArabic: 'أذكار النوم والاستيقاظ', titleEnglish: 'Sleep & Waking', iconName: 'bedtime', countHint: 'سكينة وطمأنينة'),
    DhikrCategory(id: 'prayer', titleArabic: 'أذكار بعد الصلاة', titleEnglish: 'After Prayer', iconName: 'access_time', countHint: 'دبر كل صلاة مكتوبة'),
    DhikrCategory(id: 'khatm', titleArabic: 'دعاء ختم القرآن', titleEnglish: 'Quran Khatm Dua', iconName: 'menu_book', isOccasion: true, countHint: 'عند إتمام التلاوة'),
    DhikrCategory(id: 'distress', titleArabic: 'تفريج الهم والكرب', titleEnglish: 'Anxiety & Relief', iconName: 'favorite', isOccasion: true, countHint: 'تفريج الكروب والهموم'),
    DhikrCategory(id: 'travel', titleArabic: 'أدعية السفر', titleEnglish: 'Travel Duas', iconName: 'flight', isOccasion: true, countHint: 'الحفظ في السفر'),
    DhikrCategory(id: 'istikhara', titleArabic: 'دعاء الاستخارة', titleEnglish: 'Istikhara Dua', iconName: 'psychology', isOccasion: true, countHint: 'طلب الخيرة من الله'),
    DhikrCategory(id: 'ruqyah', titleArabic: 'الرقية الشرعية', titleEnglish: 'Islamic Ruqyah', iconName: 'healing', isOccasion: true, countHint: 'شفاء وحصن حصين'),
    DhikrCategory(id: 'healing', titleArabic: 'أدعية الشفاء والمرض', titleEnglish: 'Healing & Health', iconName: 'medical_services', isOccasion: true, countHint: 'للمريض وعند الألم'),
    DhikrCategory(id: 'rizq', titleArabic: 'قضاء الدين وسعة الرزق', titleEnglish: 'Debt & Provision', iconName: 'wallet', isOccasion: true, countHint: 'تيسير الأمر والبركة'),
  ];

  static const List<DhikrItem> allAdhkar = [
    DhikrItem(
      id: 'm_1',
      categoryId: 'morning',
      title: 'آية الكرسي',
      arabicText: 'اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ.',
      reference: 'سورة البقرة: ٢٥٥',
      virtue: 'من قالها حين يصبح أجير من الجن حتى يمسي.',
      targetCount: 1,
    ),
    DhikrItem(
      id: 'm_2',
      categoryId: 'morning',
      title: 'سيد الاستغفار',
      arabicText: 'اللَّهُمَّ أَنْتَ رَبِّي لا إِلَهَ إِلا أَنْتَ ، خَلَقْتَنِي وَأَنَا عَبْدُكَ ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لا يَغْفِرُ الذُّنُوبَ إِلا أَنْتَ.',
      reference: 'صحيح البخاري',
      virtue: 'من قالها موقناً بها حين يمسي فمات دخل الجنة، وكذلك إذا أصبح.',
      targetCount: 1,
    ),
    DhikrItem(
      id: 'm_3',
      categoryId: 'morning',
      title: 'بسم الله الذي لا يضر مع اسمه شيء',
      arabicText: 'بِسْمِ اللَّهِ الَّذِي لا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الأَرْضِ وَلا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ.',
      reference: 'رواه أبو داود والترمذي',
      virtue: 'من قالها ثلاثاً لم يضره شيء.',
      targetCount: 3,
    ),
    DhikrItem(
      id: 'k_1',
      categoryId: 'khatm',
      title: 'دعاء ختم القرآن الكريم المبارك',
      arabicText: 'اللَّهُمَّ ارْحَمْنِي بِالْقُرْآنِ ، وَاجْعَلْهُ لِي إِمَامًا وَنُورًا وَهُدًى وَرَحْمَةً ، اللَّهُمَّ ذَكِّرْنِي مِنْهُ مَا نَسِيتُ ، وَعَلِّمْنِي مِنْهُ مَا جَهِلْتُ ، وَارْزُقْنِي تِلاوَتَهُ آنَاءَ اللَّيْلِ وَأَطْرَافَ النَّهَارِ ، وَاجْعَلْهُ لِي حُجَّةً يَا رَبَّ الْعَالَمِينَ.',
      reference: 'مأثور عند ختم القرآن',
      virtue: 'تتنزل الرحمات والبركات عند ختم القرآن وإجابة الدعاء.',
      targetCount: 1,
    ),
    DhikrItem(
      id: 'd_1',
      categoryId: 'distress',
      title: 'دعاء ذي النون (يونس عليه السلام)',
      arabicText: 'لَا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ.',
      reference: 'سورة الأنبياء: ٨٧',
      virtue: 'لم يدعُ بها رجل مسلم في شيء قط إلا استجاب الله له.',
      targetCount: 1,
    ),
    DhikrItem(
      id: 't_1',
      categoryId: 'travel',
      title: 'دعاء ركوب الدابة والسفر',
      arabicText: 'سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَى رَبِّنَا لَمُنقَلِبُونَ ، اللَّهُمَّ إِنَّا نَسْأَلُكَ فِي سَفَرِنَا هَذَا الْبِرَّ وَالتَّقْوَى.',
      reference: 'صحيح مسلم',
      virtue: 'حفظ المسافر وتيسير طريقه وسلامة أهله وماله.',
      targetCount: 1,
    ),
  ];
}
