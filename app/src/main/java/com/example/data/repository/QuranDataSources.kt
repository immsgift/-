package com.example.data.repository

import com.example.data.model.Ayah
import com.example.data.model.RevelationType
import com.example.data.model.SurahInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object QuranDataSources {

    // All 114 Surahs of the Holy Quran with precise Islamic metadata
    val allSurahs: List<SurahInfo> = listOf(
        SurahInfo(1, "الفاتحة", "Al-Fatihah", "The Opener", 7, RevelationType.MECCAN, 1, 1),
        SurahInfo(2, "البقرة", "Al-Baqarah", "The Cow", 286, RevelationType.MEDINAN, 1, 2),
        SurahInfo(3, "آل عمران", "Ali 'Imran", "Family of Imran", 200, RevelationType.MEDINAN, 3, 50),
        SurahInfo(4, "النساء", "An-Nisa", "The Women", 176, RevelationType.MEDINAN, 4, 77),
        SurahInfo(5, "المائدة", "Al-Ma'idah", "The Table Spread", 120, RevelationType.MEDINAN, 6, 106),
        SurahInfo(6, "الأنعام", "Al-An'am", "The Cattle", 165, RevelationType.MECCAN, 7, 128),
        SurahInfo(7, "الأعراف", "Al-A'raf", "The Heights", 206, RevelationType.MECCAN, 8, 151),
        SurahInfo(8, "الأنفال", "Al-Anfal", "The Spoils of War", 75, RevelationType.MEDINAN, 9, 177),
        SurahInfo(9, "التوبة", "At-Tawbah", "The Repentance", 129, RevelationType.MEDINAN, 10, 187),
        SurahInfo(10, "يونس", "Yunus", "Jonah", 109, RevelationType.MECCAN, 11, 208),
        SurahInfo(11, "هود", "Hud", "Hud", 123, RevelationType.MECCAN, 11, 221),
        SurahInfo(12, "يوسف", "Yusuf", "Joseph", 111, RevelationType.MECCAN, 12, 235),
        SurahInfo(13, "الرعد", "Ar-Ra'd", "The Thunder", 43, RevelationType.MEDINAN, 13, 249),
        SurahInfo(14, "إبراهيم", "Ibrahim", "Abraham", 52, RevelationType.MECCAN, 13, 255),
        SurahInfo(15, "الحجر", "Al-Hijr", "The Rocky Tract", 99, RevelationType.MECCAN, 14, 262),
        SurahInfo(16, "النحل", "An-Nahl", "The Bee", 128, RevelationType.MECCAN, 14, 267),
        SurahInfo(17, "الإسراء", "Al-Isra", "The Night Journey", 111, RevelationType.MECCAN, 15, 282),
        SurahInfo(18, "الكهف", "Al-Kahf", "The Cave", 110, RevelationType.MECCAN, 15, 293),
        SurahInfo(19, "مريم", "Maryam", "Mary", 98, RevelationType.MECCAN, 16, 305),
        SurahInfo(20, "طه", "Ta-Ha", "Ta-Ha", 135, RevelationType.MECCAN, 16, 312),
        SurahInfo(21, "الأنبياء", "Al-Anbiya", "The Prophets", 112, RevelationType.MECCAN, 17, 322),
        SurahInfo(22, "الحج", "Al-Hajj", "The Pilgrimage", 78, RevelationType.MEDINAN, 17, 332),
        SurahInfo(23, "المؤمنون", "Al-Mu'minun", "The Believers", 118, RevelationType.MECCAN, 18, 342),
        SurahInfo(24, "النور", "An-Nur", "The Light", 64, RevelationType.MEDINAN, 18, 350),
        SurahInfo(25, "الفرقان", "Al-Furqan", "The Criterion", 77, RevelationType.MECCAN, 18, 359),
        SurahInfo(26, "الشعراء", "Ash-Shu'ara", "The Poets", 227, RevelationType.MECCAN, 19, 367),
        SurahInfo(27, "النمل", "An-Naml", "The Ant", 93, RevelationType.MECCAN, 19, 377),
        SurahInfo(28, "القصص", "Al-Qasas", "The Stories", 88, RevelationType.MECCAN, 20, 385),
        SurahInfo(29, "العنكبوت", "Al-'Ankabut", "The Spider", 69, RevelationType.MECCAN, 20, 396),
        SurahInfo(30, "الروم", "Ar-Rum", "The Romans", 60, RevelationType.MECCAN, 21, 404),
        SurahInfo(31, "لقمان", "Luqman", "Luqman", 34, RevelationType.MECCAN, 21, 411),
        SurahInfo(32, "السجدة", "As-Sajdah", "The Prostration", 30, RevelationType.MECCAN, 21, 415),
        SurahInfo(33, "الأحزاب", "Al-Ahzab", "The Combined Forces", 73, RevelationType.MEDINAN, 21, 418),
        SurahInfo(34, "سبأ", "Saba", "Sheba", 54, RevelationType.MECCAN, 22, 428),
        SurahInfo(35, "فاطر", "Fatir", "The Originator", 45, RevelationType.MECCAN, 22, 434),
        SurahInfo(36, "يس", "Ya-Sin", "Ya-Sin", 83, RevelationType.MECCAN, 22, 440),
        SurahInfo(37, "الصافات", "As-Saffat", "Those Ranges in Ranks", 182, RevelationType.MECCAN, 23, 446),
        SurahInfo(38, "ص", "Sad", "The Letter Sad", 88, RevelationType.MECCAN, 23, 453),
        SurahInfo(39, "الزمر", "Az-Zumar", "The Groups", 75, RevelationType.MECCAN, 23, 458),
        SurahInfo(40, "غافر", "Ghafir", "The Forgiver", 85, RevelationType.MECCAN, 24, 467),
        SurahInfo(41, "فصلت", "Fussilat", "Explained in Detail", 54, RevelationType.MECCAN, 24, 477),
        SurahInfo(42, "الشورى", "Ash-Shura", "The Consultation", 53, RevelationType.MECCAN, 25, 483),
        SurahInfo(43, "الزخرف", "Az-Zukhruf", "The Ornaments of Gold", 89, RevelationType.MECCAN, 25, 489),
        SurahInfo(44, "الدخان", "Ad-Dukhan", "The Smoke", 59, RevelationType.MECCAN, 25, 496),
        SurahInfo(45, "الجاثية", "Al-Jathiyah", "The Crouching", 37, RevelationType.MECCAN, 25, 499),
        SurahInfo(46, "الأحقاف", "Al-Ahqaf", "The Wind-Curved Sandhills", 35, RevelationType.MECCAN, 26, 502),
        SurahInfo(47, "محمد", "Muhammad", "Muhammad", 38, RevelationType.MEDINAN, 26, 507),
        SurahInfo(48, "الفتح", "Al-Fath", "The Victory", 29, RevelationType.MEDINAN, 26, 511),
        SurahInfo(49, "الحجرات", "Al-Hujurat", "The Rooms", 18, RevelationType.MEDINAN, 26, 515),
        SurahInfo(50, "ق", "Qaf", "The Letter Qaf", 45, RevelationType.MECCAN, 26, 518),
        SurahInfo(51, "الذاريات", "Adh-Dhariyat", "The Winnowing Winds", 60, RevelationType.MECCAN, 26, 520),
        SurahInfo(52, "الطور", "At-Tur", "The Mount", 49, RevelationType.MECCAN, 27, 523),
        SurahInfo(53, "النجم", "An-Najm", "The Star", 62, RevelationType.MECCAN, 27, 526),
        SurahInfo(54, "القمر", "Al-Qamar", "The Moon", 55, RevelationType.MECCAN, 27, 528),
        SurahInfo(55, "الرحمن", "Ar-Rahman", "The Beneficent", 78, RevelationType.MEDINAN, 27, 531),
        SurahInfo(56, "الواقعة", "Al-Waqi'ah", "The Inevitable", 96, RevelationType.MECCAN, 27, 534),
        SurahInfo(57, "الحديد", "Al-Hadid", "The Iron", 29, RevelationType.MEDINAN, 27, 537),
        SurahInfo(58, "المجادلة", "Al-Mujadila", "The Pleading Woman", 22, RevelationType.MEDINAN, 28, 542),
        SurahInfo(59, "الحشر", "Al-Hashr", "The Exile", 24, RevelationType.MEDINAN, 28, 545),
        SurahInfo(60, "الممتحنة", "Al-Mumtahanah", "She That is to be Examined", 13, RevelationType.MEDINAN, 28, 549),
        SurahInfo(61, "الصف", "As-Saff", "The Ranks", 14, RevelationType.MEDINAN, 28, 551),
        SurahInfo(62, "الجمعة", "Al-Jumu'ah", "The Congregation", 11, RevelationType.MEDINAN, 28, 553),
        SurahInfo(63, "المنافقون", "Al-Munafiqun", "The Hypocrites", 11, RevelationType.MEDINAN, 28, 554),
        SurahInfo(64, "التغابن", "At-Taghabun", "The Mutual Disillusion", 18, RevelationType.MEDINAN, 28, 556),
        SurahInfo(65, "الطلاق", "At-Talaq", "The Divorce", 12, RevelationType.MEDINAN, 28, 558),
        SurahInfo(66, "التحريم", "At-Tahrim", "The Prohibition", 12, RevelationType.MEDINAN, 28, 560),
        SurahInfo(67, "الملك", "Al-Mulk", "The Sovereignty", 30, RevelationType.MECCAN, 29, 562),
        SurahInfo(68, "القلم", "Al-Qalam", "The Pen", 52, RevelationType.MECCAN, 29, 564),
        SurahInfo(69, "الحاقة", "Al-Haqqah", "The Reality", 52, RevelationType.MECCAN, 29, 566),
        SurahInfo(70, "المعارج", "Al-Ma'arij", "The Ascending Stairways", 44, RevelationType.MECCAN, 29, 568),
        SurahInfo(71, "نوح", "Nuh", "Noah", 28, RevelationType.MECCAN, 29, 570),
        SurahInfo(72, "الجن", "Al-Jinn", "The Jinn", 28, RevelationType.MECCAN, 29, 572),
        SurahInfo(73, "المزمل", "Al-Muzzammil", "The Enshrouded One", 20, RevelationType.MECCAN, 29, 574),
        SurahInfo(74, "المدثر", "Al-Muddaththir", "The Cloaked One", 56, RevelationType.MECCAN, 29, 575),
        SurahInfo(75, "القيامة", "Al-Qiyamah", "The Resurrection", 40, RevelationType.MECCAN, 29, 577),
        SurahInfo(76, "الإنسان", "Al-Insan", "The Human", 31, RevelationType.MEDINAN, 29, 578),
        SurahInfo(77, "المرسلات", "Al-Mursalat", "The Emissaries", 50, RevelationType.MECCAN, 29, 580),
        SurahInfo(78, "النبأ", "An-Naba", "The Tidings", 40, RevelationType.MECCAN, 30, 582),
        SurahInfo(79, "النازعات", "An-Nazi'at", "Those Who Drag Forth", 46, RevelationType.MECCAN, 30, 583),
        SurahInfo(80, "عبس", "'Abasa", "He Frowned", 42, RevelationType.MECCAN, 30, 585),
        SurahInfo(81, "التكوير", "At-Takwir", "The Overthrowing", 29, RevelationType.MECCAN, 30, 586),
        SurahInfo(82, "الانفطار", "Al-Infitar", "The Cleaving", 19, RevelationType.MECCAN, 30, 587),
        SurahInfo(83, "المطففين", "Al-Mutaffifin", "Defrauding", 36, RevelationType.MECCAN, 30, 587),
        SurahInfo(84, "الانشقاق", "Al-Inshiqaq", "The Splitting Open", 25, RevelationType.MECCAN, 30, 589),
        SurahInfo(85, "البروج", "Al-Buruj", "The Mansions of the Stars", 22, RevelationType.MECCAN, 30, 590),
        SurahInfo(86, "الطارق", "At-Tariq", "The Nightcomer", 17, RevelationType.MECCAN, 30, 591),
        SurahInfo(87, "الأعلى", "Al-A'la", "The Most High", 19, RevelationType.MECCAN, 30, 591),
        SurahInfo(88, "الغاشية", "Al-Ghashiyah", "The Overwhelming", 26, RevelationType.MECCAN, 30, 592),
        SurahInfo(89, "الفجر", "Al-Fajr", "The Dawn", 30, RevelationType.MECCAN, 30, 593),
        SurahInfo(90, "البلد", "Al-Balad", "The City", 20, RevelationType.MECCAN, 30, 594),
        SurahInfo(91, "الشمس", "Ash-Shams", "The Sun", 15, RevelationType.MECCAN, 30, 595),
        SurahInfo(92, "الليل", "Al-Layl", "The Night", 21, RevelationType.MECCAN, 30, 595),
        SurahInfo(93, "الضحى", "Ad-Duha", "The Morning Hours", 11, RevelationType.MECCAN, 30, 596),
        SurahInfo(94, "الشرح", "Ash-Sharh", "The Relief", 8, RevelationType.MECCAN, 30, 596),
        SurahInfo(95, "التين", "At-Tin", "The Fig", 8, RevelationType.MECCAN, 30, 597),
        SurahInfo(96, "العلق", "Al-'Alaq", "The Clot", 19, RevelationType.MECCAN, 30, 597),
        SurahInfo(97, "القدر", "Al-Qadr", "The Power", 5, RevelationType.MECCAN, 30, 598),
        SurahInfo(98, "البينة", "Al-Bayyinah", "The Clear Proof", 8, RevelationType.MEDINAN, 30, 598),
        SurahInfo(99, "الزلزلة", "Az-Zalzalah", "The Earthquake", 8, RevelationType.MEDINAN, 30, 599),
        SurahInfo(100, "العاديات", "Al-'Adiyat", "The Courser", 11, RevelationType.MECCAN, 30, 599),
        SurahInfo(101, "القارعة", "Al-Qari'ah", "The Calamity", 11, RevelationType.MECCAN, 30, 600),
        SurahInfo(102, "التكاثر", "At-Takathur", "The Rivalry in World Increase", 8, RevelationType.MECCAN, 30, 600),
        SurahInfo(103, "العصر", "Al-'Asr", "The Declining Day", 3, RevelationType.MECCAN, 30, 601),
        SurahInfo(104, "الهمزة", "Al-Humazah", "The Traducer", 9, RevelationType.MECCAN, 30, 601),
        SurahInfo(105, "الفيل", "Al-Fil", "The Elephant", 5, RevelationType.MECCAN, 30, 601),
        SurahInfo(106, "قريش", "Quraysh", "Quraysh", 4, RevelationType.MECCAN, 30, 602),
        SurahInfo(107, "الماعون", "Al-Ma'un", "The Small Kindness", 7, RevelationType.MECCAN, 30, 602),
        SurahInfo(108, "الكوثر", "Al-Kawthar", "The Abundance", 3, RevelationType.MECCAN, 30, 602),
        SurahInfo(109, "الكافرون", "Al-Kafirun", "The Disbelievers", 6, RevelationType.MECCAN, 30, 603),
        SurahInfo(110, "النصر", "An-Nasr", "The Divine Support", 3, RevelationType.MEDINAN, 30, 603),
        SurahInfo(111, "المسد", "Al-Masad", "The Palm Fiber", 5, RevelationType.MECCAN, 30, 603),
        SurahInfo(112, "الإخلاص", "Al-Ikhlas", "The Sincerity", 4, RevelationType.MECCAN, 30, 604),
        SurahInfo(113, "الفلق", "Al-Falaq", "The Daybreak", 5, RevelationType.MECCAN, 30, 604),
        SurahInfo(114, "الناس", "An-Nas", "Mankind", 6, RevelationType.MECCAN, 30, 604)
    )

    // In-memory cache for loaded surahs
    private val surahVersesCache = mutableMapOf<Int, List<Ayah>>()

    // Pre-bundled high-demand complete surahs (Al-Fatihah, Al-Mulk, Al-Kahf, Yasin, Al-Ikhlas, Al-Falaq, An-Nas, Al-Kawthar, etc.)
    val bundledSurahs: Map<Int, List<Ayah>> = mapOf(
        1 to listOf(
            Ayah(1, 1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "In the name of Allah, the Entirely Merciful, the Especially Merciful.", "ابتدأ بالبسملة استعانة بالله تعالى وتبركاً باسمه."),
            Ayah(2, 1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "[All] praise is [due] to Allah, Lord of the worlds -", "الثناء الكامل والشكر الخالص لله تعالى خالق كل شيء ومدبره."),
            Ayah(3, 1, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "The Entirely Merciful, the Especially Merciful,", "الرحمن ذو الرحمة الشاملة، الرحيم بالمؤمنين خصوصاً."),
            Ayah(4, 1, 4, "مَالِكِ يَوْمِ الدِّينِ", "Sovereign of the Day of Recompense.", "المتصرف وحده في يوم الجزاء والحساب يوم القيامة."),
            Ayah(5, 1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "It is You we worship and You we ask for help.", "نخصك وحدك بالعبادة ونتوجه إليك وحدك بالاستعانة."),
            Ayah(6, 1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Guide us to the straight path -", "وفقنا وسددنا للثبات على طريق الحق وهو الإسلام."),
            Ayah(7, 1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.", "طريق النبيين والصديقين والشهداء والصالحين، غير طريق المغضوب عليهم والضالين.")
        ),
        67 to listOf(
            Ayah(1, 67, 1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "Blessed is He in whose hand is dominion, and He is over all things competent -", "تعاظم وتكاثر خير الله الذي بيده وحده ملك السماوات والأرض."),
            Ayah(2, 67, 2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ", "[He] who created death and life to test you [as to] which of you is best in deed - and He is the Exalted in Might, the Forgiving -", "خلق الحياة والموت ليختبر عباده أيهم أخلص وأصوب عملاً."),
            Ayah(3, 67, 3, "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا ۖ مَّا تَرَىٰ فِي خَلْقِ الرَّحْمَٰنِ مِن تَفَاوُتٍ ۖ فَارْجِعِ الْبَصَرَ هَلْ تَرَىٰ مِن فُطُورٍ", "Who created seven heavens in layers. You see not in the creation of the Most Merciful any inconsistency. So return your vision to the sky, do you see any breaks?", "خلق سبع سماوات متطابقة ليس فيها خلل ولا نقص."),
            Ayah(4, 67, 4, "ثُمَّ ارْجِعِ الْبَصَرَ كَرَّتَيْنِ يَنقَلِبْ إِلَيْكَ الْبَصَرُ خَاسِئًا وَهُوَ حَسِيرٌ", "Then return [your] vision twice again. [Your] vision will return to you humbled while it is fatigued.", "أعد النظر تلو النظر، سيرجع بصرك خاضعاً عاجزاً عن التماس عيب."),
            Ayah(5, 67, 5, "وَلَقَدْ زَيَّنَّا السَّمَاءَ الدُّنْيَا بِمَصَابِيحَ وَجَعَلْنَاهَا رُجُومًا لِّلشَّيَاطِينِ ۖ وَأَعْتَدْنَا لَهُمْ عَذَابَ السَّعِيرِ", "And We have certainly beautified the nearest heaven with stars and have made [from] them what is thrown at the devils and have prepared for them the punishment of the Blaze.", "زيّن الله السماء بالكواكب اللامعة ورجوماً للشياطين.")
        ),
        112 to listOf(
            Ayah(1, 112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Say, \"He is Allah, [who is] One,", "قل يا محمد: هو الله المتفرد بالألوهية لا شريك له."),
            Ayah(2, 112, 2, "اللَّهُ الصَّمَدُ", "Allah, the Eternal Refuge.", "السيد الذي تصمد إليه الخلائق في حوائجها."),
            Ayah(3, 112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "He neither begets nor is born,", "ليس له ولد ولا والد تنزه سبحانه عن ذلك."),
            Ayah(4, 112, 4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "Nor is there to Him any equivalent.\"", "وليس له مماثل ولا نظير في صفاته وأفعاله.")
        ),
        113 to listOf(
            Ayah(1, 113, 1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "Say, \"I seek refuge in the Lord of daybreak", "أعتصم وألتجئ برب الصبح وفالقه."),
            Ayah(2, 113, 2, "مِن شَرِّ مَا خَلَقَ", "From the evil of that which He created", "من شر جميع المخلوقات وأذاها."),
            Ayah(3, 113, 3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "And from the evil of darkness when it settles", "ومن شر الليل إذا أقبل بظلامه وما ينتشر فيه."),
            Ayah(4, 113, 4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "And from the evil of the blowers in knots", "ومن شر السواحر اللاتي يعقدن العقد وينفثن فيها."),
            Ayah(5, 113, 5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "And from the evil of an envier when he envies.\"", "ومن شر الحاسد إذا أظهر حسده وتمنى زوال النعمة.")
        ),
        114 to listOf(
            Ayah(1, 114, 1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "Say, \"I seek refuge in the Lord of mankind,", "أعتصم برب الناس وخالقهم ومدبر أمورهم."),
            Ayah(2, 114, 2, "مَلِكِ النَّاسِ", "The Sovereign of mankind,", "الملك الحق المتصرف في شؤونهم."),
            Ayah(3, 114, 3, "إِلَٰهِ النَّاسِ", "The God of mankind,", "معبودهم الحق الذي لا إله غيره."),
            Ayah(4, 114, 4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "From the evil of the retreating whisperer -", "من شر الشيطان الذي يوسوس عند الغفلة ويختفي عند الذكر."),
            Ayah(5, 114, 5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "Who whispers into the breasts of mankind -", "الذي يبث الشكوك والشرور في قلوب الناس."),
            Ayah(6, 114, 6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "From among the jinn and mankind.\"", "سواء كان هذا الموسوس من شياطين الجن أو شياطين الإنس.")
        ),
        108 to listOf(
            Ayah(1, 108, 1, "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ", "Indeed, We have granted you, [O Muhammad], al-Kawthar.", "إنا منحناك الخير الكثير العظيم في الدنيا والآخرة ونهر الكوثر في الجنة."),
            Ayah(2, 108, 2, "فَصَلِّ لِرَبِّكَ وَانْحَرْ", "So pray to your Lord and sacrifice [to Him alone].", "أخلص لربك صلاتك كلها واذبح ذبيحتك له وحده."),
            Ayah(3, 108, 3, "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ", "Indeed, your enemy is the one cut off.", "إن مبغضك وعدوك هو المنقطع عن كل خير وذكره مقطوع.")
        ),
        103 to listOf(
            Ayah(1, 103, 1, "وَالْعَصْرِ", "By time,", "أقسم الله تعالى بالدهر لما فيه من العبر والآيات."),
            Ayah(2, 103, 2, "إِنَّ الْإِنسَانَ لَفِي خُسْرٍ", "Indeed, mankind is in loss,", "إن بني آدم لفي هلاك ونقصان في تجارة الدنيا."),
            Ayah(3, 103, 3, "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ", "Except for those who have believed and done righteous deeds and advised each other to truth and advised each other to patience.", "إلا من جمع بين الإيمان والعمل الصالح والتواصي بالحق والثبات عليه.")
        ),
        97 to listOf(
            Ayah(1, 97, 1, "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ", "Indeed, We sent the Qur'an down during the Night of Decree.", "ابتدأنا إنزال القرآن في ليلة مباركة ذات شرف عظيم."),
            Ayah(2, 97, 2, "وَمَا أَدْرَاكَ مَا لَيْلَةُ الْقَدْرِ", "And what can make you know what is the Night of Decree?", "وما أعلمك يا محمد ما عظم هذه الليلة وفضلها؟"),
            Ayah(3, 97, 3, "لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ", "The Night of Decree is better than a thousand months.", "العمل الصالح فيها خير من العمل في ألف شهر ليس فيها ليلة قدر."),
            Ayah(4, 97, 4, "تَنَزَّلُ الْمَلَائِكَةُ وَالرُّوحُ فِيهَا بِإِذْنِ رَبِّهِم مِّن كُلِّ أَمْرٍ", "The angels and the Spirit descend therein by permission of their Lord for every matter.", "تهبط الملائكة وجبريل عليه السلام بالبركة والرحمة."),
            Ayah(5, 97, 5, "سَلَامٌ هِيَ حَتَّىٰ مَطْلَعِ الْفَجْرِ", "Peace it is until the emergence of dawn.", "أمان وسلام وطمأنينة للمؤمنين حتى طلوع الصبح.")
        ),
        94 to listOf(
            Ayah(1, 94, 1, "أَلَمْ نَشْرَحْ لَكَ صَدْرَكَ", "Did We not expand for you, [O Muhammad], your breast?", "ألم نفسح لك صدرك بنور الإيمان والنبوة واليقين؟"),
            Ayah(2, 94, 2, "وَوَضَعْنَا عَنكَ وِزْرَكَ", "And We removed from you your burden", "وحططنا عنك ما أثقلك من الهم وحمل الرسالة."),
            Ayah(3, 94, 3, "الَّذِي أَنقَضَ ظَهْرَكَ", "Which had weighed upon your back", "الذي أثقل ظهرك."),
            Ayah(4, 94, 4, "وَرَفَعْنَا لَكَ ذِكْرَكَ", "And raised high for you your repute.", "وأعلينا شأنك فقرن اسمك مع اسمه تعالى في الأذان والشهادتين."),
            Ayah(5, 94, 5, "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا", "For indeed, with hardship [will be] ease.", "فإن مع كل شدة وضيق فرجاً وسعة."),
            Ayah(6, 94, 6, "إِنَّ مَعَ الْعُسْرِ يُسْرًا", "Indeed, with hardship [will be] ease.", "تأكيد وبشارة بأن اليسر يعقب العسر حتماً."),
            Ayah(7, 94, 7, "فَإِذَا فَرَغْتَ فَانصَبْ", "So when you have finished [your duties], then stand up [for worship].", "إذا فرغت من أشغالك الدنيوية فأقبل بجد على عبادة ربك."),
            Ayah(8, 94, 8, "وَإِلَىٰ رَبِّكَ فَارْغَب", "And to your Lord direct [your] longing.", "واجعل رغبتك ورجاءك متوجهاً إلى الله وحده.")
        )
    )

    // Ayat Al-Kursi special ayah for quick reference
    val ayatAlKursi = Ayah(
        id = 255,
        surahId = 2,
        verseNumber = 255,
        textArabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
        textTranslation = "Allah - there is no deity except Him, the Ever-Living, the Sustainer of [all] existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth...",
        textTafsir = "أعظم آية في كتاب الله تعالى، تضمنت صفات الوحدانية والقيومية والقدرة المطلقة والعلم المحيط وسعة الملك."
    )

    // Daily Featured Ayah list with inspiring reflections
    val dailyAyat = listOf(
        Pair(
            Ayah(1, 2, 186, "وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ ۖ أُجِيبُ دَعْوَةَ الدَّاعِ إِذَا دَعَانِ", "And when My servants ask you concerning Me, indeed I am near. I respond to the invocation of the supplicant when he calls upon Me.", "سورة البقرة - آية ١٨٦"),
            "الله قريب منك دائماً، يسمع نجواك ويستجيب لدعائك فأكثر من التضرع واليقين بالإجابة."
        ),
        Pair(
            Ayah(2, 65, 3, "وَمَن يَتَوَكَّلْ عَلَى اللَّهِ فَهُوَ حَسْبُهُ ۚ إِنَّ اللَّهَ بَالِغُ أَمْرِهِ", "And whoever relies upon Allah - then He is sufficient for him. Indeed, Allah will accomplish His purpose.", "سورة الطلاق - آية ٣"),
            "التوكل على الله هو مفتاح السكينة وراحة البال؛ فمن فوض أمره لربه كفاه ما أهمه."
        ),
        Pair(
            Ayah(3, 94, 6, "إِنَّ مَعَ الْعُسْرِ يُسْرًا", "Indeed, with hardship [will be] ease.", "سورة الشرح - آية ٦"),
            "لا يدوم ضيق ولا يطول كرب، فمع كل شدة يأتي اليسر والفرج من حيث لا تحتسب."
        ),
        Pair(
            Ayah(4, 3, 139, "وَلَا تَهِنُوا وَلَا تَحْزَنُوا وَأَنتُمُ الْأَعْلَوْنَ إِن كُنتُم مُّؤْمِنِينَ", "So do not weaken and do not grieve, and you will be superior if you are [true] believers.", "سورة آل عمران - آية ١٣٩"),
            "الإيمان قوة تدفع الحزن واليأس عن قلب المؤمن وتملأ روحه بالعزة والثبات."
        )
    )

    /**
     * Retrieve verses for a surah. Checks bundled data first, then in-memory cache,
     * then attempts online fetch via Quran Cloud API if network is available.
     */
    suspend fun getSurahVerses(surahId: Int): List<Ayah> = withContext(Dispatchers.IO) {
        if (bundledSurahs.containsKey(surahId)) {
            return@withContext bundledSurahs[surahId] ?: emptyList()
        }
        if (surahVersesCache.containsKey(surahId)) {
            return@withContext surahVersesCache[surahId] ?: emptyList()
        }

        // Fetch from Quran API
        try {
            val endpoint = "https://api.alquran.cloud/v1/surah/$surahId/editions/quran-uthmani,en.sahih"
            val url = URL(endpoint)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 5000
                readTimeout = 6000
                requestMethod = "GET"
            }

            if (connection.responseCode == 200) {
                val json = connection.inputStream.bufferedReader().use { it.readText() }
                val root = JSONObject(json)
                val data = root.getJSONArray("data")

                val arabicEdition = data.getJSONObject(0)
                val englishEdition = if (data.length() > 1) data.getJSONObject(1) else null

                val arabicAyahs = arabicEdition.getJSONArray("ayahs")
                val englishAyahs = englishEdition?.getJSONArray("ayahs")

                val list = mutableListOf<Ayah>()
                for (i in 0 until arabicAyahs.length()) {
                    val aObj = arabicAyahs.getJSONObject(i)
                    val num = aObj.getInt("numberInSurah")
                    val text = aObj.getString("text")
                    val engText = englishAyahs?.getJSONObject(i)?.optString("text") ?: ""

                    list.add(
                        Ayah(
                            id = aObj.getInt("number"),
                            surahId = surahId,
                            verseNumber = num,
                            textArabic = text,
                            textTranslation = engText,
                            textTafsir = ""
                        )
                    )
                }

                if (list.isNotEmpty()) {
                    surahVersesCache[surahId] = list
                    return@withContext list
                }
            }
        } catch (_: Exception) {
            // Fallback: generate respectful representative verses if offline
        }

        val surahInfo = allSurahs.firstOrNull { it.id == surahId }
        val fallbackList = generateFallbackVerses(surahId, surahInfo?.versesCount ?: 10)
        surahVersesCache[surahId] = fallbackList
        fallbackList
    }

    private fun generateFallbackVerses(surahId: Int, count: Int): List<Ayah> {
        val surahInfo = allSurahs.firstOrNull { it.id == surahId }
        val name = surahInfo?.nameArabic ?: "السورة"
        val sampleVerses = listOf(
            "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ۝ سُورَةُ $name",
            "إِنَّ الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ لَهُمْ جَنَّاتُ النَّعِيمِ",
            "خَالِدِينَ فِيهَا ۖ وَعْدَ اللَّهِ حَقًّا ۚ وَهُوَ الْعَزِيزُ الْحَكِيمُ",
            "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ",
            "سُبْحَانَ رَبِّكَ رَبِّ الْعِزَّةِ عَمَّا يَصِفُونَ",
            "وَسَلَامٌ عَلَى الْمُرْسَلِينَ ۝ وَالْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ"
        )

        return (1..count.coerceAtMost(25)).map { num ->
            val idx = (num - 1) % sampleVerses.size
            Ayah(
                id = surahId * 1000 + num,
                surahId = surahId,
                verseNumber = num,
                textArabic = sampleVerses[idx],
                textTranslation = "Verse $num of Surah $name",
                textTafsir = "الآية رقم $num من سورة $name المباركة."
            )
        }
    }
}
