package com.example.data.repository

import com.example.data.model.DhikrCategory
import com.example.data.model.DhikrItem

object AdhkarDataSources {

    // Main Categories
    val categories: List<DhikrCategory> = listOf(
        // Daily Adhkar
        DhikrCategory("morning", "أذكار الصباح", "Morning Adhkar", "wb_sunny", false, "تبدأ بعد صلاة الفجر"),
        DhikrCategory("evening", "أذكار المساء", "Evening Adhkar", "nights_stay", false, "تبدأ بعد صلاة العصر"),
        DhikrCategory("sleep", "أذكار النوم والاستيقاظ", "Sleep & Waking", "bedtime", false, "سكينة وطمأنينة"),
        DhikrCategory("prayer", "أذكار بعد الصلاة", "After Prayer", "access_time", false, "دبر كل صلاة مكتوبة"),
        DhikrCategory("mosque", "أذكار المسجد والوضوء", "Mosque & Ablution", "mosque", false, "طهارة ونور"),
        DhikrCategory("home", "أذكار المنزل والخروج", "Home & Outside", "home", false, "حفظ وبركة"),

        // Occasions and Situations Duas
        DhikrCategory("khatm", "دعاء ختم القرآن", "Quran Khatm Dua", "menu_book", true, "عند إتمام التلاوة"),
        DhikrCategory("distress", "تفريج الهم والكرب", "Anxiety & Relief", "favorite", true, "تفريج الكروب والهموم"),
        DhikrCategory("travel", "أدعية السفر", "Travel Duas", "flight", true, "الحفظ في السفر"),
        DhikrCategory("istikhara", "دعاء الاستخارة", "Istikhara Dua", "psychology", true, "طلب الخيرة من الله"),
        DhikrCategory("ruqyah", "الرقية الشرعية", "Islamic Ruqyah", "healing", true, "شفاء وحصن حصين"),
        DhikrCategory("healing", "أدعية الشفاء والمرض", "Healing & Health", "medical_services", true, "للمريض وعند الألم"),
        DhikrCategory("rizq", "قضاء الدين وسعة الرزق", "Debt & Provision", "account_balance_wallet", true, "جلب الرزق وتيسير الأمر"),
        DhikrCategory("nature", "المطر والرعد والرياح", "Rain & Thunder", "thunderstorm", true, "عند تقلب الأحوال الجوية"),
        DhikrCategory("fasting", "أدعية الصيام والإفطار", "Fasting & Iftar", "ramadan", true, "في رمضان وأيام الصيام")
    )

    // Complete rich list of Adhkar and Duas with authentic sources and virtues
    val allAdhkarItems: List<DhikrItem> = listOf(
        // === أذكار الصباح ===
        DhikrItem(
            id = "m_1",
            categoryId = "morning",
            title = "آية الكرسي",
            arabicText = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ.",
            translation = "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence...",
            reference = "سورة البقرة: ٢٥٥ - رواه الحاكم وصححه الألباني",
            virtue = "من قالها حين يصبح أجير من الجن حتى يمسي.",
            targetCount = 1
        ),
        DhikrItem(
            id = "m_2",
            categoryId = "morning",
            title = "سيد الاستغفار",
            arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لا إِلَهَ إِلا أَنْتَ ، خَلَقْتَنِي وَأَنَا عَبْدُكَ ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لا يَغْفِرُ الذُّنُوبَ إِلا أَنْتَ.",
            translation = "O Allah, You are my Lord, none has the right to be worshiped but You. You created me and I am Your servant...",
            reference = "صحيح البخاري",
            virtue = "من قالها موقناً بها حين يمسي فمات من ليلته دخل الجنة، وكذلك إذا أصبح.",
            targetCount = 1
        ),
        DhikrItem(
            id = "m_3",
            categoryId = "morning",
            title = "أصبحنا وأصبح الملك لله",
            arabicText = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ ، وَالْحَمْدُ لِلَّهِ ، لا إِلَهَ إِلا اللَّهُ وَحْدَهُ لا شَرِيكَ لَهُ ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ ، رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَذَا الْيَوْمِ وَخَيْرَ مَا بَعْدَهُ ، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هَذَا الْيَوْمِ وَشَرِّ مَا بَعْدَهُ ، رَبِّ أَعُوذُ بِكَ مِنَ الْكَسَلِ وَسُوءِ الْكِبَرِ ، رَبِّ أَعُوذُ بِكَ مِنْ عَذَابٍ فِي النَّارِ وَعَذَابٍ فِي الْقَبْرِ.",
            translation = "We have entered a new morning and with it all dominion belongs to Allah. All praise is due to Allah...",
            reference = "صحيح مسلم",
            virtue = "سؤال الله خير اليوم والتعوذ من الكسل وعذاب القبر والنار.",
            targetCount = 1
        ),
        DhikrItem(
            id = "m_4",
            categoryId = "morning",
            title = "بسم الله الذي لا يضر مع اسمه شيء",
            arabicText = "بِسْمِ اللَّهِ الَّذِي لا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الأَرْضِ وَلا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ.",
            translation = "In the Name of Allah with whose Name nothing can cause harm in earth or heaven...",
            reference = "رواه أبو داود والترمذي",
            virtue = "من قالها ثلاثاً إذا أصبح وثلاثاً إذا أمسى لم يضره شيء.",
            targetCount = 3
        ),
        DhikrItem(
            id = "m_5",
            categoryId = "morning",
            title = "رضيت بالله رباً",
            arabicText = "رَضِيتُ بِاللَّهِ رَبًّا ، وَبِالإِسْلامِ دِينًا ، وَبِمُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا.",
            translation = "I am pleased with Allah as my Lord, with Islam as my religion, and with Muhammad as my Prophet.",
            reference = "رواه أبو داود وأحمد والترمذي",
            virtue = "من قالها حين يصبح وحين يمسي كان حقاً على الله أن يرضيه يوم القيامة.",
            targetCount = 3
        ),
        DhikrItem(
            id = "m_6",
            categoryId = "morning",
            title = "يا حي يا قيوم برحمتك أستغيث",
            arabicText = "يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ ، أَصْلِحْ لِي شَأْنِي كُلَّهُ ، وَلا تَكِلْنِي إِلَى نَفْسِي طَرْفَةَ عَيْنٍ.",
            translation = "O Ever Living One, O Eternal One, by Your mercy I call on You; set right for me all my affairs and do not leave me to myself even for the blink of an eye.",
            reference = "رواه الحاكم وصححه الألباني",
            virtue = "تفويض الأمر لله تعالى وإصلاح الحال في كل الشؤون.",
            targetCount = 1
        ),
        DhikrItem(
            id = "m_7",
            categoryId = "morning",
            title = "حسبي الله لا إله إلا هو",
            arabicText = "حَسْبِيَ اللَّهُ لا إِلَهَ إِلا هُوَ عَلَيْهِ تَوَكَّلْتُ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ.",
            translation = "Allah is sufficient for me. There is no deity except Him. On Him I have relied, and He is the Lord of the Great Throne.",
            reference = "رواه أبو داود",
            virtue = "من قالها سبع مرات حين يصبح وحين يمسي كفاه الله ما أهمه من أمر الدنيا والآخرة.",
            targetCount = 7
        ),
        DhikrItem(
            id = "m_8",
            categoryId = "morning",
            title = "التسبيح والحمد العظيم",
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ : عَدَدَ خَلْقِهِ ، وَرِضَا نَفْسِهِ ، وَزِنَةَ عَرْشِهِ ، وَمِدَادَ كَلِمَاتِهِ.",
            translation = "Glory is to Allah and praise is to Him, by the number of His creation and by His pleasure and by the weight of His Throne...",
            reference = "صحيح مسلم",
            virtue = "تزن في الأجر ساعات طويلة من الذكر.",
            targetCount = 3
        ),

        // === أذكار المساء ===
        DhikrItem(
            id = "e_1",
            categoryId = "evening",
            title = "أمسينا وأمسى الملك لله",
            arabicText = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ ، وَالْحَمْدُ لِلَّهِ ، لا إِلَهَ إِلا اللَّهُ وَحْدَهُ لا شَرِيكَ لَهُ ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ ، رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَذِهِ اللَّيْلَةِ وَخَيْرَ مَا بَعْدَهَا ، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هَذِهِ اللَّيْلَةِ وَشَرِّ مَا بَعْدَهَا.",
            translation = "We have reached the evening and at this very time unto Allah belongs all sovereignty...",
            reference = "صحيح مسلم",
            virtue = "الحفظ من شرور الليل والتماس خيراته وبركته.",
            targetCount = 1
        ),
        DhikrItem(
            id = "e_2",
            categoryId = "evening",
            title = "سيد الاستغفار في المساء",
            arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لا إِلَهَ إِلا أَنْتَ ، خَلَقْتَنِي وَأَنَا عَبْدُكَ ، وَأَنَا عَلَى عهدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لا يَغْفِرُ الذُّنُوبَ إِلا أَنْتَ.",
            translation = "O Allah! You are my Lord! None has the right to be worshiped but You...",
            reference = "صحيح البخاري",
            virtue = "من قالها حين يمسي فمات دخل الجنة.",
            targetCount = 1
        ),
        DhikrItem(
            id = "e_3",
            categoryId = "evening",
            title = "الاستعاذة بكلمات الله التامات",
            arabicText = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ.",
            translation = "I seek refuge in the perfect words of Allah from the evil of that which He has created.",
            reference = "صحيح مسلم",
            virtue = "من قالها ثلاثاً لم تضره حمة (سم ولدغ) تلك الليلة.",
            targetCount = 3
        ),
        DhikrItem(
            id = "e_4",
            categoryId = "evening",
            title = "المعوذتان وسورة الإخلاص",
            arabicText = "قُلْ هُوَ اللَّهُ أَحَدٌ ۝ قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ۝ قُلْ أَعُوذُ بِرَبِّ النَّاسِ.",
            translation = "Recitation of Surah Al-Ikhlas, Al-Falaq and An-Nas (three times each).",
            reference = "رواه أبو داود والترمذي",
            virtue = "تكفيك من كل شيء.",
            targetCount = 3
        ),
        DhikrItem(
            id = "e_5",
            categoryId = "evening",
            title = "طلب العافية والستر",
            arabicText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَفْوَ وَالْعَافِيَةَ فِي الدُّنْيَا وَالآخِرَةِ ، اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَفْوَ وَالْعَافِيَةَ فِي دِينِي وَدُنْيَايَ وَأَهْلِي وَمَالِي ، اللَّهُمَّ اسْتُرْ عَوْرَاتِي وَآمِنْ رَوْعَاتِي ، اللَّهُمَّ احْفَظْنِي مِنْ بَيْنِ يَدَيَّ وَمِنْ خَلْفِي وَعَنْ يَمِينِي وَعَنْ شِمَالِي وَمِنْ فَوْقِي ، وَأَعُوذُ بِعَظَمَتِكَ أَنْ أُغْتَالَ مِنْ تَحْتِي.",
            translation = "O Allah, I ask You for pardon and well-being in this life and the next...",
            reference = "رواه أبو داود وابن ماجه وصححه الألباني",
            virtue = "دعاء الحفظ الشامل من كل الجهات والنوازل.",
            targetCount = 1
        ),

        // === أذكار النوم والاستيقاظ ===
        DhikrItem(
            id = "s_1",
            categoryId = "sleep",
            title = "باسمك ربي وضعت جنبي",
            arabicText = "بِاسْمِكَ رَبِّي وَضَعْتُ جَنْبِي ، وَبِكَ أَرْفَعُهُ ، فَإِنْ أَمْسَكْتَ نَفْسِي فَارْحَمْهَا ، وَإِنْ أَرْسَلْتَهَا فَاحْفَظْهَا بِمَا تَحْفَظُ بِهِ عِبَادَكَ الصَّالِحِينَ.",
            translation = "In Your name my Lord, I lie down and in Your name I arise...",
            reference = "صحيح البخاري ومسلم",
            virtue = "تسليم الروح لله تعالى وحفظها في النوم.",
            targetCount = 1
        ),
        DhikrItem(
            id = "s_2",
            categoryId = "sleep",
            title = "اللهم قني عذابك يوم تبعث عبادك",
            arabicText = "اللَّهُمَّ قِنِي عَذَابَكَ يَوْمَ تَبْعَثُ عِبَادَكَ.",
            translation = "O Allah, save me from Your punishment on the Day that You resurrect Your slaves.",
            reference = "رواه أبو داود والترمذي",
            virtue = "كان رسول الله ﷺ يضع يده اليمنى تحت خده ويقولها ثلاثاً.",
            targetCount = 3
        ),
        DhikrItem(
            id = "s_3",
            categoryId = "sleep",
            title = "دعاء الاستيقاظ من النوم",
            arabicText = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ.",
            translation = "All praise is for Allah who gave us life after having taken it from us and unto Him is the resurrection.",
            reference = "صحيح البخاري ومسلم",
            virtue = "حمد الله على تجدد الحياة واليقظة بعد الموت الأصغر.",
            targetCount = 1
        ),

        // === أذكار بعد الصلاة ===
        DhikrItem(
            id = "p_1",
            categoryId = "prayer",
            title = "الاستغفار وطلب السلام",
            arabicText = "أَسْتَغْفِرُ اللَّهَ ، أَسْتَغْفِرُ اللَّهَ ، أَسْتَغْفِرُ اللَّهَ ، اللَّهُمَّ أَنْتَ السَّلامُ وَمِنْكَ السَّلامُ ، تَبَارَكْتَ يَا ذَا الْجَلالِ وَالإِكْرَامِ.",
            translation = "I seek Allah's forgiveness (three times). O Allah, You are Peace and from You comes peace...",
            reference = "صحيح مسلم",
            virtue = "أول ما يستفتح به المسلم ذكره بعد السلام من الصلاة المكتوبة.",
            targetCount = 1
        ),
        DhikrItem(
            id = "p_2",
            categoryId = "prayer",
            title = "التهليل والتوحيد دبر الصلاة",
            arabicText = "لا إِلَهَ إِلا اللَّهُ وَحْدَهُ لا شَرِيكَ لَهُ ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ ، اللَّهُمَّ لا مَانِعَ لِمَا أَعْطَيْتَ ، وَلا مُعْطِيَ لِمَا مَنَعْتَ ، وَلا يَنْفَعُ ذَا الْجَدِّ مِنْكَ الْجَدُّ.",
            translation = "None has the right to be worshiped but Allah alone, Who has no partner...",
            reference = "صحيح البخاري ومسلم",
            virtue = "إقرار بكمال قدرة الله وتفرده بالعطاء والمنع.",
            targetCount = 1
        ),
        DhikrItem(
            id = "p_3",
            categoryId = "prayer",
            title = "التسبيح والتحميد والتكبير (٣٣ مرة)",
            arabicText = "سُبْحَانَ اللَّهِ (٣٣) ، الْحَمْدُ لِلَّهِ (٣٣) ، اللَّهُ أَكْبَرُ (٣٣) ، وتَمامُ المائةِ: لا إِلَهَ إِلا اللَّهُ وَحْدَهُ لا شَرِيكَ لَهُ ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
            translation = "Subhanallah (33), Alhamdulillah (33), Allahu Akbar (33), and to complete 100: La ilaha illallah...",
            reference = "صحيح مسلم",
            virtue = "غُفرت خطاياه وإن كانت مثل زبد البحر.",
            targetCount = 33
        ),

        // === أدعية تفريج الهم والكرب والحزن ===
        DhikrItem(
            id = "d_1",
            categoryId = "distress",
            title = "دعاء ذي النون (يونس عليه السلام)",
            arabicText = "لَا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ.",
            translation = "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers.",
            reference = "سورة الأنبياء: ٨٧ - رواه الترمذي وأحمد وصححه الألباني",
            virtue = "دعوة ذي النون إذ دعا بها في بطن الحوت لم يدعُ بها رجل مسلم في شيء قط إلا استجاب الله له.",
            targetCount = 1
        ),
        DhikrItem(
            id = "d_2",
            categoryId = "distress",
            title = "دعاء تفريج الهم والحزن العظيم",
            arabicText = "اللَّهُمَّ إِنِّي عَبْدُكَ ، ابْنُ عَبْدِكَ ، ابْنُ أَمَتِكَ ، نَاصِيَتِي بِيَدِكَ ، مَاضٍ فِيَّ حُكْمُكَ ، عَدْلٌ فِيَّ قَضَاؤُكَ ، أَسْأَلُكَ بِكُلِّ اسْمٍ هُوَ لَكَ ، سَمَّيْتَ بِهِ نَفْسَكَ ، أَوْ أَنْزَلْتَهُ فِي كِتَابِكَ ، أَوْ عَلَّمْتَهُ أَحَدًا مِنْ خَلْقِكَ ، أَوِ اسْتَأْثَرْتَ بِهِ فِي عِلْمِ الْغَيْبِ عِنْدَكَ ، أَنْ تَجْعَلَ الْقُرْآنَ رَبِيعَ قَلْبِي ، وَنُورَ صَدْرِي ، وَجَلاءَ حُزْنِي ، وَذَهَابَ هَمِّي.",
            translation = "O Allah, I am Your slave and the son of Your male and female slave...",
            reference = "رواه أحمد وصححه الألباني",
            virtue = "ما أصاب أحداً قط هم ولا حزن فقال هذا الدعاء إلا أذهب الله همه وأبدله مكانه فرحاً.",
            targetCount = 1
        ),
        DhikrItem(
            id = "d_3",
            categoryId = "distress",
            title = "دعاء الكرب النبوي",
            arabicText = "لا إِلَهَ إِلا اللَّهُ الْعَظِيمُ الْحَلِيمُ ، لا إِلَهَ إِلا اللَّهُ رَبُّ الْعَرْشِ الْعَظِيمِ ، لا إِلَهَ إِلا اللَّهُ رَبُّ السَّمَاوَاتِ وَرَبُّ الأَرْضِ وَرَبُّ الْعَرْشِ الْكَرِيمِ.",
            translation = "There is no deity except Allah, the Magnificent, the Forbearing...",
            reference = "صحيح البخاري ومسلم",
            virtue = "كان النبي ﷺ يدعو به عند الكرب والشدة.",
            targetCount = 1
        ),

        // === دعاء ختم القرآن الكريم ===
        DhikrItem(
            id = "k_1",
            categoryId = "khatm",
            title = "دعاء ختم القرآن الكريم المبارك",
            arabicText = "اللَّهُمَّ ارْحَمْنِي بِالْقُرْآنِ ، وَاجْعَلْهُ لِي إِمَامًا وَنُورًا وَهُدًى وَرَحْمَةً ، اللَّهُمَّ ذَكِّرْنِي مِنْهُ مَا نَسِيتُ ، وَعَلِّمْنِي مِنْهُ مَا جَهِلْتُ ، وَارْزُقْنِي تِلاوَتَهُ آنَاءَ اللَّيْلِ وَأَطْرَافَ النَّهَارِ ، وَاجْعَلْهُ لِي حُجَّةً يَا رَبَّ الْعَالَمِينَ ، اللَّهُمَّ أَصْلِحْ لِي دِينِي الَّذِي هُوَ عِصْمَةُ أَمْرِي ، وَأَصْلِحْ لِي دُنْيَايَ الَّتِي فِيهَا مَعَاشِي ، وَأَصْلِحْ لِي آخِرَتِي الَّتِي فِيهَا مَعَادِي ، وَاجْعَلِ الْحَيَاةَ زِيَادَةً لِي فِي كُلِّ خَيْرٍ ، وَاجْعَلِ الْمَوْتَ رَاحَةً لِي مِنْ كُلِّ شَرٍّ.",
            translation = "O Allah, have mercy on me through the Qur'an, and make it for me a guide, a light, a direction, and a mercy...",
            reference = "مأثور عند ختم القرآن الكريم",
            virtue = "تتنزل الرحمات والبركات عند ختم القرآن وإجابة الدعاء.",
            targetCount = 1
        ),

        // === أدعية السفر ===
        DhikrItem(
            id = "t_1",
            categoryId = "travel",
            title = "دعاء ركوب الدابة والسفر",
            arabicText = "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَى رَبِّنَا لَمُنقَلِبُونَ ، اللَّهُمَّ إِنَّا نَسْأَلُكَ فِي سَفَرِنَا هَذَا الْبِرَّ وَالتَّقْوَى ، وَمِنَ الْعَمَلِ مَا تَرْضَى ، اللَّهُمَّ هَوِّنْ عَلَيْنَا سَفَرَنَا هَذَا وَاطْوِ عَنَّا بُعْدَهُ ، اللَّهُمَّ أَنْتَ الصَّاحِبُ فِي السَّفَرِ وَالْخَلِيفَةُ فِي الأَهْلِ.",
            translation = "Glory to Him who has subjected this to us, and we could never have it by our efforts...",
            reference = "صحيح مسلم",
            virtue = "حفظ المسافر وتيسير طريقه وسلامة أهله وماله.",
            targetCount = 1
        ),

        // === دعاء الاستخارة ===
        DhikrItem(
            id = "i_1",
            categoryId = "istikhara",
            title = "دعاء صلاة الاستخارة النبوي",
            arabicText = "اللَّهُمَّ إِنِّي أَسْتَخِيرُكَ بِعِلْمِكَ ، وَأَسْتَقْدِرُكَ بِقُدْرَتِكَ ، وَأَسْأَلُكَ مِنْ فَضْلِكَ الْعَظِيمِ ، فَإِنَّكَ تَقْدِرُ وَلا أَقْدِرُ ، وَتَعْلَمُ وَلا أَعْلَمُ ، وَأَنْتَ عَلامُ الْغُيُوبِ ، اللَّهُمَّ إِنْ كُنْتَ تَعْلَمُ أَنَّ هَذَا الأَمْرَ خَيْرٌ لِي فِي دِينِي وَمَعَاشِي وَعَاقِبَةِ أَمْرِي فَاقْدُرْهُ لِي وَيَسِّرْهُ لِي ثُمَّ بَارِكْ لِي فِيهِ ، وَإِنْ كُنْتَ تَعْلَمُ أَنَّ هَذَا الأَمْرَ شَرٌّ لِي فِي دِينِي وَمَعَاشِي وَعَاقِبَةِ أَمْرِي فَاصْرِفْهُ عَنِّي وَاصْرِفْنِي عَنْهُ وَاقْدُرْ لِيَ الْخَيْرَ حَيْثُ كَانَ ثُمَّ أَرْضِنِي بِهِ.",
            translation = "O Allah, I seek Your counsel through Your knowledge, and I seek ability through Your power...",
            reference = "صحيح البخاري",
            virtue = "كان النبي ﷺ يعلم أصحابه الاستخارة في الأمور كلها كما يعلمهم السورة من القرآن.",
            targetCount = 1
        ),

        // === الرقية الشرعية ===
        DhikrItem(
            id = "r_1",
            categoryId = "ruqyah",
            title = "الفاتحة الشافية الكافية",
            arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ۝ الرَّحْمَٰنِ الرَّحِيمِ ۝ مَالِكِ يَوْمِ الدِّينِ ۝ إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ ۝ اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ ۝ صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ.",
            translation = "Surah Al-Fatihah, the greatest healing surah.",
            reference = "صحيح البخاري ومسلم",
            virtue = "أم الكتاب والسبع المثاني والرقية الشافية من كل داء وعين وسحر.",
            targetCount = 7
        ),
        DhikrItem(
            id = "r_2",
            categoryId = "ruqyah",
            title = "رقية جبريل عليه السلام للنبي ﷺ",
            arabicText = "بِسْمِ اللَّهِ أَرْقِيكَ ، مِنْ كُلِّ شَيْءٍ يُؤْذِيكَ ، مِنْ شَرِّ كُلِّ نَفْسٍ أَوْ عَيْنِ حَاسِدٍ اللَّهُ يَشْفِيكَ ، بِسْمِ اللَّهِ أَرْقِيكَ.",
            translation = "In the Name of Allah I recite over you, from every thing that harms you...",
            reference = "صحيح مسلم",
            virtue = "رقية جبريل للنبي عليه الصلاة والسلام وهي حصن عظيم من العين والحسد والأذى.",
            targetCount = 3
        ),
        DhikrItem(
            id = "r_3",
            categoryId = "ruqyah",
            title = "دعاء وضع اليد على موضع الألم",
            arabicText = "بِسْمِ اللَّهِ (ثَلاثًا) ، أَعُوذُ بِاللَّهِ وَقُدْرَتِهِ مِنْ شَرِّ مَا أَجِدُ وَأُحَاذِرُ (سَبْعَ مَرَّاتٍ).",
            translation = "Bismillah (3 times), I seek refuge in Allah and His power from the evil of what I find and fear (7 times).",
            reference = "صحيح مسلم",
            virtue = "يضع المسلم يده على موضع الألم من جسده ويرقي نفسه فيبرأ بإذن الله.",
            targetCount = 7
        ),

        // === أدعية الشفاء والعيادة ===
        DhikrItem(
            id = "h_1",
            categoryId = "healing",
            title = "دعاء عيادة المريض الجامع",
            arabicText = "أَسْأَلُ اللَّهَ الْعَظِيمَ رَبَّ الْعَرْشِ الْعَظِيمِ أَنْ يَشْفِيَكَ.",
            translation = "I ask Allah the Magnificent, the Lord of the Magnificent Throne, to cure you.",
            reference = "رواه أبو داود والترمذي وصححه الألباني",
            virtue = "ما من عبد مسلم يعود مريضاً لم يحضر أجله فيقول سبع مرات إلا عافاه الله.",
            targetCount = 7
        ),
        DhikrItem(
            id = "h_2",
            categoryId = "healing",
            title = "اللهم رب الناس أذهب البأس",
            arabicText = "اللَّهُمَّ رَبَّ النَّاسِ ، أَذْهِبِ الْبَاسَ ، اشْفِ أَنْتَ الشَّافِي ، لا شِفَاءَ إِلا شِفَاؤُكَ ، شِفَاءً لا يُغَادِرُ سَقَمًا.",
            translation = "O Allah, Lord of mankind, do away with the harm, heal, You are the Healer...",
            reference = "صحيح البخاري ومسلم",
            virtue = "كان النبي ﷺ يمسح بيمينه على المريض ويدعو به.",
            targetCount = 1
        ),

        // === قضاء الدين وسعة الرزق ===
        DhikrItem(
            id = "rz_1",
            categoryId = "rizq",
            title = "دعاء قضاء الدين حتى لو كان مثل جبل",
            arabicText = "اللَّهُمَّ اكْفِنِي بِحَلالِكَ عَنْ حَرَامِكَ ، وَأَغْنِنِي بِفَضْلِكَ عَمَّنْ سِوَاكَ.",
            translation = "O Allah, suffice me with what You have allowed instead of what You have forbidden, and make me independent of all others besides You.",
            reference = "رواه الترمذي وحسنه",
            virtue = "لو كان عليك مثل جبل دين لأداه الله عنك.",
            targetCount = 3
        ),
        DhikrItem(
            id = "rz_2",
            categoryId = "rizq",
            title = "دعاء الاستعاذة من غلبة الدين وقهر الرجال",
            arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ ، وَأَعُوذُ بِكَ مِنَ الْعَجْزِ وَالْكَسَلِ ، وَأَعُوذُ بِكَ مِنَ الْجُبْنِ وَالْبُخْلِ ، وَأَعُوذُ بِكَ مِنْ غَلَبَةِ الدَّيْنِ وَقَهْرِ الرِّجَالِ.",
            translation = "O Allah, I seek refuge in You from anxiety and grief, helplessness and laziness...",
            reference = "صحيح البخاري",
            virtue = "تفريج الهم وقضاء الديون المتراكمة.",
            targetCount = 1
        ),

        // === المطر والرعد والرياح ===
        DhikrItem(
            id = "n_1",
            categoryId = "nature",
            title = "دعاء نزول المطر المبارك",
            arabicText = "اللَّهُمَّ صَيِّبًا نَافِعًا ، مُطِرْنَا بِفَضْلِ اللَّهِ وَرَحْمَتِهِ.",
            translation = "O Allah, make it a beneficial rain. We have received rain by the grace and mercy of Allah.",
            reference = "صحيح البخاري",
            virtue = "طلب النفع والبركة من الغيث ونسبته لفضل الله.",
            targetCount = 1
        ),
        DhikrItem(
            id = "n_2",
            categoryId = "nature",
            title = "دعاء سماع صوت الرعد",
            arabicText = "سُبْحَانَ الَّذِي يُسَبِّحُ الرَّعْدُ بِحَمْدِهِ وَالْمَلائِكَةُ مِنْ خِيفَتِهِ.",
            translation = "Glory is to Him whom the thunder glorifies with His praise, and likewise the angels in awe of Him.",
            reference = "موطأ مالك وصححه الألباني",
            virtue = "تعظيم الله عند سماع آياته المخوفة في السماء.",
            targetCount = 1
        ),

        // === أدعية الصيام والإفطار ===
        DhikrItem(
            id = "f_1",
            categoryId = "fasting",
            title = "دعاء الإفطار عند فطر الصائم",
            arabicText = "ذَهَبَ الظَّمَأُ ، وَابْتَلَّتِ الْعُرُوقُ ، وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ.",
            translation = "The thirst has gone, the veins are moistened, and the reward is confirmed, if Allah wills.",
            reference = "رواه أبو داود وصححه الألباني",
            virtue = "دعوة الصائم عند فطره لا تُرد.",
            targetCount = 1
        ),
        DhikrItem(
            id = "f_2",
            categoryId = "fasting",
            title = "دعاء ليلة القدر",
            arabicText = "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي.",
            translation = "O Allah, You are Forgiving, You love forgiveness, so forgive me.",
            reference = "رواه الترمذي وصححه",
            virtue = "علم النبي ﷺ عائشة رضي الله عنها أن تقوله في أرجى الليالي.",
            targetCount = 7
        ),

        // === أذكار المسجد والوضوء ===
        DhikrItem(
            id = "mq_1",
            categoryId = "mosque",
            title = "دعاء دخول المسجد",
            arabicText = "بِسْمِ اللَّهِ ، وَالصَّلاةُ وَالسَّلامُ عَلَى رَسُولِ اللَّهِ ، اللَّهُمَّ افْتَحْ لِي أَبْوَابَ رَحْمَتِكَ.",
            translation = "In the Name of Allah, and prayers and peace upon the Messenger of Allah. O Allah, open for me the doors of Your mercy.",
            reference = "صحيح مسلم وأبو داود",
            virtue = "استفتاح بيوت الله بالأدب والتماس الرحمة.",
            targetCount = 1
        ),
        DhikrItem(
            id = "mq_2",
            categoryId = "mosque",
            title = "الذكر بعد الفراغ من الوضوء",
            arabicText = "أَشْهَدُ أَنْ لا إِلَهَ إِلا اللَّهُ وَحْدَهُ لا شَرِيكَ لَهُ ، وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ ، اللَّهُمَّ اجْعَلْنِي مِنَ التَّوَّابِينَ وَاجْعَلْنِي مِنَ الْمُتَطَهِّرِينَ.",
            translation = "I testify that there is no god but Allah alone with no partner, and I testify that Muhammad is His slave and Messenger...",
            reference = "صحيح مسلم والترمذي",
            virtue = "فتحت له أبواب الجنة الثمانية يدخل من أيها شاء.",
            targetCount = 1
        ),

        // === أذكار المنزل ===
        DhikrItem(
            id = "hm_1",
            categoryId = "home",
            title = "دعاء الخروج من المنزل",
            arabicText = "بِسْمِ اللَّهِ ، تَوَكَّلْتُ عَلَى اللَّهِ ، وَلا حَوْلَ وَلا قُوَّةَ إِلا بِاللَّهِ.",
            translation = "In the name of Allah, I trust in Allah, and there is no might and no power except in Allah.",
            reference = "رواه أبو داود والترمذي",
            virtue = "يقال له: هُديت وكُفيت ووُقيت، وتنحى عنه الشيطان.",
            targetCount = 1
        ),
        DhikrItem(
            id = "hm_2",
            categoryId = "home",
            title = "دعاء دخول المنزل",
            arabicText = "بِسْمِ اللَّهِ وَلَجْنَا ، وَبِسْمِ اللَّهِ خَرَجْنَا ، وَعَلَى اللَّهِ رَبِّنَا تَوَكَّلْنَا ، ثُمَّ لِيُسَلِّمْ عَلَى أَهْلِهِ.",
            translation = "In the name of Allah we enter, and in the name of Allah we leave, and upon our Lord we depend.",
            reference = "رواه أبو داود",
            virtue = "إذا ذكر اسم الله عند دخوله وطعامه قال الشيطان: لا مبيت لكم ولا عشاء.",
            targetCount = 1
        )
    )
}
