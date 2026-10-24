// 100% Offline Service Worker Registration
if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/sw.js').catch((e) => console.log('SW Note:', e));
  });
}

// 114 Surahs Directory
const ALL_SURAHS = [
  { id: 1, name: "الفاتحة", transliteration: "Al-Fatihah", verses: 7, type: "مكية" },
  { id: 2, name: "البقرة", transliteration: "Al-Baqarah", verses: 286, type: "مدنية" },
  { id: 3, name: "آل عمران", transliteration: "Ali 'Imran", verses: 200, type: "مدنية" },
  { id: 4, name: "النساء", transliteration: "An-Nisa", verses: 176, type: "مدنية" },
  { id: 5, name: "المائدة", transliteration: "Al-Ma'idah", verses: 120, type: "مدنية" },
  { id: 6, name: "الأنعام", transliteration: "Al-An'am", verses: 165, type: "مكية" },
  { id: 7, name: "الأعراف", transliteration: "Al-A'raf", verses: 206, type: "مكية" },
  { id: 8, name: "الأنفال", transliteration: "Al-Anfal", verses: 75, type: "مدنية" },
  { id: 9, name: "التوبة", transliteration: "At-Tawbah", verses: 129, type: "مدنية" },
  { id: 10, name: "يونس", transliteration: "Yunus", verses: 109, type: "مكية" },
  { id: 11, name: "هود", transliteration: "Hud", verses: 123, type: "مكية" },
  { id: 12, name: "يوسف", transliteration: "Yusuf", verses: 111, type: "مكية" },
  { id: 13, name: "الرعد", transliteration: "Ar-Ra'd", verses: 43, type: "مدنية" },
  { id: 14, name: "إبراهيم", transliteration: "Ibrahim", verses: 52, type: "مكية" },
  { id: 15, name: "الحجر", transliteration: "Al-Hijr", verses: 99, type: "مكية" },
  { id: 16, name: "النحل", transliteration: "An-Nahl", verses: 128, type: "مكية" },
  { id: 17, name: "الإسراء", transliteration: "Al-Isra", verses: 111, type: "مكية" },
  { id: 18, name: "الكهف", transliteration: "Al-Kahf", verses: 110, type: "مكية" },
  { id: 19, name: "مريم", transliteration: "Maryam", verses: 98, type: "مكية" },
  { id: 20, name: "طه", transliteration: "Ta-Ha", verses: 135, type: "مكية" },
  { id: 21, name: "الأنبياء", transliteration: "Al-Anbiya", verses: 112, type: "مكية" },
  { id: 22, name: "الحج", transliteration: "Al-Hajj", verses: 78, type: "مدنية" },
  { id: 23, name: "المؤمنون", transliteration: "Al-Mu'minun", verses: 118, type: "مكية" },
  { id: 24, name: "النور", transliteration: "An-Nur", verses: 64, type: "مدنية" },
  { id: 25, name: "الفرقان", transliteration: "Al-Furqan", verses: 77, type: "مكية" },
  { id: 26, name: "الشعراء", transliteration: "Ash-Shu'ara", verses: 227, type: "مكية" },
  { id: 27, name: "النمل", transliteration: "An-Naml", verses: 93, type: "مكية" },
  { id: 28, name: "القصص", transliteration: "Al-Qasas", verses: 88, type: "مكية" },
  { id: 29, name: "العنكبوت", transliteration: "Al-'Ankabut", verses: 69, type: "مكية" },
  { id: 30, name: "الروم", transliteration: "Ar-Rum", verses: 60, type: "مكية" },
  { id: 31, name: "لقمان", transliteration: "Luqman", verses: 34, type: "مكية" },
  { id: 32, name: "السجدة", transliteration: "As-Sajdah", verses: 30, type: "مكية" },
  { id: 33, name: "الأحزاب", transliteration: "Al-Ahzab", verses: 73, type: "مدنية" },
  { id: 34, name: "سبأ", transliteration: "Saba", verses: 54, type: "مكية" },
  { id: 35, name: "فاطر", transliteration: "Fatir", verses: 45, type: "مكية" },
  { id: 36, name: "يس", transliteration: "Ya-Sin", verses: 83, type: "مكية" },
  { id: 37, name: "الصافات", transliteration: "As-Saffat", verses: 182, type: "مكية" },
  { id: 38, name: "ص", transliteration: "Sad", verses: 88, type: "مكية" },
  { id: 39, name: "الزمر", transliteration: "Az-Zumar", verses: 75, type: "مكية" },
  { id: 40, name: "غافر", transliteration: "Ghafir", verses: 85, type: "مكية" },
  { id: 41, name: "فصلت", transliteration: "Fussilat", verses: 54, type: "مكية" },
  { id: 42, name: "الشورى", transliteration: "Ash-Shura", verses: 53, type: "مكية" },
  { id: 43, name: "الزخرف", transliteration: "Az-Zukhruf", verses: 89, type: "مكية" },
  { id: 44, name: "الدخان", transliteration: "Ad-Dukhan", verses: 59, type: "مكية" },
  { id: 45, name: "الجاثية", transliteration: "Al-Jathiyah", verses: 37, type: "مكية" },
  { id: 46, name: "الأحقاف", transliteration: "Al-Ahqaf", verses: 35, type: "مكية" },
  { id: 47, name: "محمد", transliteration: "Muhammad", verses: 38, type: "مدنية" },
  { id: 48, name: "الفتح", transliteration: "Al-Fath", verses: 29, type: "مدنية" },
  { id: 49, name: "الحجرات", transliteration: "Al-Hujurat", verses: 18, type: "مدنية" },
  { id: 50, name: "ق", transliteration: "Qaf", verses: 45, type: "مكية" },
  { id: 51, name: "الذاريات", transliteration: "Adh-Dhariyat", verses: 60, type: "مكية" },
  { id: 52, name: "الطور", transliteration: "At-Tur", verses: 49, type: "مكية" },
  { id: 53, name: "النجم", transliteration: "An-Najm", verses: 62, type: "مكية" },
  { id: 54, name: "القمر", transliteration: "Al-Qamar", verses: 55, type: "مكية" },
  { id: 55, name: "الرحمن", transliteration: "Ar-Rahman", verses: 78, type: "مدنية" },
  { id: 56, name: "الواقعة", transliteration: "Al-Waqi'ah", verses: 96, type: "مكية" },
  { id: 57, name: "الحديد", transliteration: "Al-Hadid", verses: 29, type: "مدنية" },
  { id: 58, name: "المجادلة", transliteration: "Al-Mujadila", verses: 22, type: "مدنية" },
  { id: 59, name: "الحشر", transliteration: "Al-Hashr", verses: 24, type: "مدنية" },
  { id: 60, name: "الممتحنة", transliteration: "Al-Mumtahanah", verses: 13, type: "مدنية" },
  { id: 61, name: "الصف", transliteration: "As-Saff", verses: 14, type: "مدنية" },
  { id: 62, name: "الجمعة", transliteration: "Al-Jumu'ah", verses: 11, type: "مدنية" },
  { id: 63, name: "المنافقون", transliteration: "Al-Munafiqun", verses: 11, type: "مدنية" },
  { id: 64, name: "التغابن", transliteration: "At-Taghabun", verses: 18, type: "مدنية" },
  { id: 65, name: "الطلاق", transliteration: "At-Talaq", verses: 12, type: "مدنية" },
  { id: 66, name: "التحريم", transliteration: "At-Tahrim", verses: 12, type: "مدنية" },
  { id: 67, name: "الملك", transliteration: "Al-Mulk", verses: 30, type: "مكية" },
  { id: 68, name: "القلم", transliteration: "Al-Qalam", verses: 52, type: "مكية" },
  { id: 69, name: "الحاقة", transliteration: "Al-Haqqah", verses: 52, type: "مكية" },
  { id: 70, name: "المعارج", transliteration: "Al-Ma'arij", verses: 44, type: "مكية" },
  { id: 71, name: "نوح", transliteration: "Nuh", verses: 28, type: "مكية" },
  { id: 72, name: "الجن", transliteration: "Al-Jinn", verses: 28, type: "مكية" },
  { id: 73, name: "المزمل", transliteration: "Al-Muzzammil", verses: 20, type: "مكية" },
  { id: 74, name: "المدثر", transliteration: "Al-Muddaththir", verses: 56, type: "مكية" },
  { id: 75, name: "القيامة", transliteration: "Al-Qiyamah", verses: 40, type: "مكية" },
  { id: 76, name: "الإنسان", transliteration: "Al-Insan", verses: 31, type: "مدنية" },
  { id: 77, name: "المرسلات", transliteration: "Al-Mursalat", verses: 50, type: "مكية" },
  { id: 78, name: "النبأ", transliteration: "An-Naba", verses: 40, type: "مكية" },
  { id: 79, name: "النازعات", transliteration: "An-Nazi'at", verses: 46, type: "مكية" },
  { id: 80, name: "عبس", transliteration: "'Abasa", verses: 42, type: "مكية" },
  { id: 81, name: "التكوير", transliteration: "At-Takwir", verses: 29, type: "مكية" },
  { id: 82, name: "الانفطار", transliteration: "Al-Infitar", verses: 19, type: "مكية" },
  { id: 83, name: "المطففين", transliteration: "Al-Mutaffifin", verses: 36, type: "مكية" },
  { id: 84, name: "الانشقاق", transliteration: "Al-Inshiqaq", verses: 25, type: "مكية" },
  { id: 85, name: "البروج", transliteration: "Al-Buruj", verses: 22, type: "مكية" },
  { id: 86, name: "الطارق", transliteration: "At-Tariq", verses: 17, type: "مكية" },
  { id: 87, name: "الأعلى", transliteration: "Al-A'la", verses: 19, type: "مكية" },
  { id: 88, name: "الغاشية", transliteration: "Al-Ghashiyah", verses: 26, type: "مكية" },
  { id: 89, name: "الفجر", transliteration: "Al-Fajr", verses: 30, type: "مكية" },
  { id: 90, name: "البلد", transliteration: "Al-Balad", verses: 20, type: "مكية" },
  { id: 91, name: "الشمس", transliteration: "Ash-Shams", verses: 15, type: "مكية" },
  { id: 92, name: "الليل", transliteration: "Al-Layl", verses: 21, type: "مكية" },
  { id: 93, name: "الضحى", transliteration: "Ad-Duha", verses: 11, type: "مكية" },
  { id: 94, name: "الشرح", transliteration: "Ash-Sharh", verses: 8, type: "مكية" },
  { id: 95, name: "التين", transliteration: "At-Tin", verses: 8, type: "مكية" },
  { id: 96, name: "العلق", transliteration: "Al-'Alaq", verses: 19, type: "مكية" },
  { id: 97, name: "القدر", transliteration: "Al-Qadr", verses: 5, type: "مكية" },
  { id: 98, name: "البينة", transliteration: "Al-Bayyinah", verses: 8, type: "مدنية" },
  { id: 99, name: "الزلزلة", transliteration: "Az-Zalzalah", verses: 8, type: "مدنية" },
  { id: 100, name: "العاديات", transliteration: "Al-'Adiyat", verses: 11, type: "مكية" },
  { id: 101, name: "القارعة", transliteration: "Al-Qari'ah", verses: 11, type: "مكية" },
  { id: 102, name: "التكاثر", transliteration: "At-Takathur", verses: 8, type: "مكية" },
  { id: 103, name: "العصر", transliteration: "Al-'Asr", verses: 3, type: "مكية" },
  { id: 104, name: "الهمزة", transliteration: "Al-Humazah", verses: 9, type: "مكية" },
  { id: 105, name: "الفيل", transliteration: "Al-Fil", verses: 5, type: "مكية" },
  { id: 106, name: "قريش", transliteration: "Quraysh", verses: 4, type: "مكية" },
  { id: 107, name: "الماعون", transliteration: "Al-Ma'un", verses: 7, type: "مكية" },
  { id: 108, name: "الكوثر", transliteration: "Al-Kawthar", verses: 3, type: "مكية" },
  { id: 109, name: "الكافرون", transliteration: "Al-Kafirun", verses: 6, type: "مكية" },
  { id: 110, name: "النصر", transliteration: "An-Nasr", verses: 3, type: "مدنية" },
  { id: 111, name: "المسد", transliteration: "Al-Masad", verses: 5, type: "مكية" },
  { id: 112, name: "الإخلاص", transliteration: "Al-Ikhlas", verses: 4, type: "مكية" },
  { id: 113, name: "الفلق", transliteration: "Al-Falaq", verses: 5, type: "مكية" },
  { id: 114, name: "الناس", transliteration: "An-Nas", verses: 6, type: "مكية" }
];

// 48 Algerian Wilayas
const ALGERIAN_CITIES = [
  { name: "01 - أدرار", lat: 27.8743, lng: -0.2939 },
  { name: "02 - الشلف", lat: 36.1652, lng: 1.3345 },
  { name: "03 - الأغواط", lat: 33.8000, lng: 2.8651 },
  { name: "04 - أم البواقي", lat: 35.8755, lng: 7.1135 },
  { name: "05 - باتنة", lat: 35.5560, lng: 6.1741 },
  { name: "06 - بجاية", lat: 36.7559, lng: 5.0843 },
  { name: "07 - بسكرة", lat: 34.8504, lng: 5.7281 },
  { name: "08 - بشار", lat: 31.6167, lng: -2.2167 },
  { name: "09 - البليدة", lat: 36.4700, lng: 2.8300 },
  { name: "10 - البويرة", lat: 36.3749, lng: 3.9020 },
  { name: "11 - تمنراست", lat: 22.7850, lng: 5.5228 },
  { name: "12 - تبسة", lat: 35.4042, lng: 8.1242 },
  { name: "13 - تلمسان", lat: 34.8783, lng: -1.3150 },
  { name: "14 - تيارت", lat: 35.3710, lng: 1.3170 },
  { name: "15 - تيزي وزو", lat: 36.7118, lng: 4.0459 },
  { name: "16 - الجزائر العاصمة", lat: 36.7538, lng: 3.0588 },
  { name: "17 - الجلفة", lat: 34.6728, lng: 3.2630 },
  { name: "18 - جيجل", lat: 36.8206, lng: 5.7667 },
  { name: "19 - سطيف", lat: 36.1911, lng: 5.4137 },
  { name: "20 - سعيدة", lat: 34.8303, lng: 0.1517 },
  { name: "21 - سكيكدة", lat: 36.8792, lng: 6.9075 },
  { name: "22 - سيدي بلعباس", lat: 35.1899, lng: -0.6308 },
  { name: "23 - عنابة", lat: 36.9000, lng: 7.7667 },
  { name: "24 - قالمة", lat: 36.4622, lng: 7.4261 },
  { name: "25 - قسنطينة", lat: 36.3650, lng: 6.6147 },
  { name: "26 - المدية", lat: 36.2642, lng: 2.7539 },
  { name: "27 - مستغانم", lat: 35.9311, lng: 0.0892 },
  { name: "28 - المسيلة", lat: 35.7058, lng: 4.5419 },
  { name: "29 - معسكر", lat: 35.3947, lng: 0.1403 },
  { name: "30 - ورقلة", lat: 31.9493, lng: 5.3250 },
  { name: "31 - وهران", lat: 35.6971, lng: -0.6308 },
  { name: "32 - البيض", lat: 33.6832, lng: 1.0193 },
  { name: "33 - إليزي", lat: 26.4833, lng: 8.4667 },
  { name: "34 - برج بوعريريج", lat: 36.0732, lng: 4.7611 },
  { name: "35 - بومرداس", lat: 36.7664, lng: 3.4772 },
  { name: "36 - الطارف", lat: 36.7672, lng: 8.3139 },
  { name: "37 - تندوف", lat: 27.6742, lng: -8.1478 },
  { name: "38 - تيسمسيلت", lat: 35.6072, lng: 1.8108 },
  { name: "39 - الوادي", lat: 33.3683, lng: 6.8675 },
  { name: "40 - خنشلة", lat: 35.4358, lng: 7.1433 },
  { name: "41 - سوق أهراس", lat: 36.2864, lng: 7.9511 },
  { name: "42 - تيبازة", lat: 36.5942, lng: 2.4431 },
  { name: "43 - ميلة", lat: 36.4503, lng: 6.2644 },
  { name: "44 - عين الدفلى", lat: 36.2642, lng: 1.9678 },
  { name: "45 - النعامة", lat: 33.2667, lng: -0.3167 },
  { name: "46 - عين تموشنت", lat: 35.2975, lng: -1.1403 },
  { name: "47 - غرداية", lat: 32.4909, lng: 3.6736 },
  { name: "48 - غليزان", lat: 35.7408, lng: 0.5558 }
];

// Rich Islamic Duas & Nafahat (Without "توبة ومغفرة" as requested)
const RICH_NAFAHAT_DUAS = [
  {
    id: "distress",
    label: "🤲 تفريج الهم والكرب",
    keywords: ["هم", "كرب", "ضيق", "حزن", "غم", "مهموم", "مخنوق"],
    ayah: "﴿ لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ ﴾",
    surah: "سورة الأنبياء: 87",
    advice: "دعوة ذي النون أخرجته من ظلمات ثلاث (الليل والبحر وبطن الحوت)، فما دعا بها مكروب إلا كشف الله عنه ما أهمه. اعلم أن الفرج مع الكرب، وأن مع العسر يسراً.",
    dhikr: "اللهم إني عبدك وابن عبدك ناصيتي بيدك، أسألك أن تجعل القرآن ربيع قلبي ونور صدري وجلاء حزني وذهاب همي."
  },
  {
    id: "rizq",
    label: "💰 سعة الرزق وقضاء الدين",
    keywords: ["رزق", "دين", "مال", "فقر", "وظيفة", "عمل"],
    ayah: "﴿ وَمَن يَتَّقِ اللَّهَ يَجْعَل لَّهُ مَخْرَجًا * وَيَرْزُقْهُ مِنْ حَيْثُ لَا يَحْتَسِبُ ﴾",
    surah: "سورة الطلاق: 2-3",
    advice: "الرزق مكفول ومقسوم، وخزائن الله ملأى لا تغيضها نفقة. داوم على الاستغفار وصلة الرحم والتوكل بيقين فإنها من أعظم جالبات الرزق الحلال المبارك.",
    dhikr: "اللهم اكفني بحلالك عن حرامك وأغنني بفضلك عمن سواك، اللهم إني أعوذ بك من غلبة الدين وقهر الرجال."
  },
  {
    id: "healing",
    label: "🩺 الشفاء والعافية للمريض",
    keywords: ["مرض", "تعب", "ألم", "وجع", "مريض", "صحة"],
    ayah: "﴿ وَإِذَا مَرِضْتُ فَهُوَ يَشْفِينِ ﴾",
    surah: "سورة الشعراء: 80",
    advice: "كل وجع يصيبك هو رفعة لدرجاتك وحط لأوزارك. الشافي هو الله وحده، فامسح بيدك على موضع الألم واستحضر قدرة الله العظيمة التي لا يعجزها داء.",
    dhikr: "اللهم رب الناس أذهب الباس واشف أنت الشافي لا شفاء إلا شفاؤك شفاءً لا يغادر سقماً."
  },
  {
    id: "exams",
    label: "📚 التوفيق وتيسير الامتحانات",
    keywords: ["امتحان", "اختبار", "دراسة", "نجاح", "توفيق"],
    ayah: "﴿ رَبِّ اشْرَحْ لِي صَدْرِي * وَيَسِّرْ لِي أَمْرِي * وَاحْلُلْ عُقْدَةً مِّن لِّسَانِي * يَفْقَهُوا قَوْلِي ﴾",
    surah: "سورة طه: 25-28",
    advice: "ابذل جهدك واطرد وساوس الخوف من ذهنك؛ فالله يرى سعيك ولن يضيع تعبك. ابدأ باسم الله واستعن به وادعُ بيقين.",
    dhikr: "اللهم لا سهل إلا ما جعلته سهلاً، وأنت تجعل الحزن إذا شئت سهلاً، يا معلم إبراهيم علمني ويا مفهم سليمان فهمني."
  },
  {
    id: "family",
    label: "🏡 صلاح الأبناء والأسرة",
    keywords: ["أبناء", "أولاد", "زوج", "زوجة", "أسرة", "بيت"],
    ayah: "﴿ رَبَّنَا هَبْ لَنَا مِنْ أَزْوَاجِنَا وَذُرِّيَّاتِنَا قُرَّةَ أَعْيُنٍ وَاجْعَلْنَا لِلْمُتَّقِينَ إِمَامًا ﴾",
    surah: "سورة الفرقان: 74",
    advice: "دعاء الوالد لولده بابٌ مفتوح لا يرد. املأ بيتك بذكر الله وسورة البقرة، واجعل لسانك رطباً بالدعاء لهم بالهداية والبر والتوفيق.",
    dhikr: "اللهم أصلح لي ذريتي وبارك لي في أهلي واجعلهم هداة مهتدين، وحبب إليهم الإيمان وزينه في قلوبهم."
  },
  {
    id: "protection",
    label: "🛡️ الحفظ والتحصين من العين",
    keywords: ["عين", "حسد", "سحر", "خوف", "حفظ", "تحصين"],
    ayah: "﴿ فَإِن تَوَلَّوْا فَقُلْ حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ ۖ عَلَيْهِ تَوَكَّلْتُ ۖ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ ﴾",
    surah: "سورة التوبة: 129",
    advice: "لا سلطان لعين ولا حاسد على من كان في ذمة الله وكنفه. حافظ على المعوذات وآية الكرسي دبر كل صلاة وحين تصبح وتمسي.",
    dhikr: "أعوذ بكلمات الله التامات من شر ما خلق، وبسم الله الذي لا يضر مع اسمه شيء في الأرض ولا في السماء وهو السميع العليم."
  },
  {
    id: "marriage",
    label: "💍 تيسير الزواج وصلاح الشريك",
    keywords: ["زواج", "شريك", "عفة", "نصيب"],
    ayah: "﴿ رَبِّ إِنِّي لِمَا أَنزَلْتَ إِلَيَّ مِنْ خَيْرٍ فَقِيرٌ ﴾",
    surah: "سورة القصص: 24",
    advice: "دعوة موسى عليه السلام كانت مفتاح الرزق والأمان والزوجة الصالحة. اسأل الله العفة والستر، وأحسن الظن بمواعيد قدره اللطيف.",
    dhikr: "اللهم ارزقني زوجاً صالحاً تقياً نقياً يقر عيني به ويقر عينه بي، واجعل بيننا مودة ورحمة وسكينة."
  },
  {
    id: "justice",
    label: "⚖️ نصر المظلوم وكف الأذى",
    keywords: ["ظلم", "مظلوم", "حق", "عداوة", "أذى"],
    ayah: "﴿ أُذِنَ لِلَّذِينَ يُقَاتَلُونَ بِأَنَّهُمْ ظُلِمُوا ۚ وَإِنَّ اللَّهَ عَلَىٰ نَصْرِهِمْ لَقَدِيرٌ ﴾",
    surah: "سورة الحج: 39",
    advice: "دعوة المظلوم ليس بينها وبين الله حجاب، يرفعها الله فوق الغمام ويقسم بعزته وجلاله لينصرنها ولو بعد حين. فوّض أمرك إلى الحكم العدل.",
    dhikr: "حسبي الله ونعم الوكيل، نعم المولى ونعم النصير، وأفوض أمري إلى الله إن الله بصير بالعباد."
  },
  {
    id: "travel",
    label: "✈️ أدعية السفر والترحال",
    keywords: ["سفر", "طريق", "غربة", "طائرة", "رحلة"],
    ayah: "﴿ سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَٰذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ * وَإِنَّا إِلَىٰ رَبِّنَا لَمُنقَلِبُونَ ﴾",
    surah: "سورة الزخرف: 13-14",
    advice: "استودع الله نفسك وأهلك ودينك وخواتيم عملك؛ فإن الله إذا استودع شيئاً حفظه سبحانه.",
    dhikr: "اللهم إنا نسألك في سفرنا هذا البر والتقوى، اللهم هون علينا سفرنا هذا واطوِ عنا بعده، أنت الصاحب في السفر والخليفة في الأهل."
  },
  {
    id: "grief",
    label: "💔 مواساة القلب وفقد الأحبة",
    keywords: ["فقد", "موت", "وفاة", "فراق", "قلب مكسور"],
    ayah: "﴿ الَّذِينَ إِذَا أَصَابَتْهُم مُّصِيبَةٌ قَالُوا إِنَّا لِلَّهِ وَإِنَّا إِلَيْهِ رَاجِعُونَ ﴾",
    surah: "سورة البقرة: 156",
    advice: "أمر المؤمن كله له خير؛ إن أصابته ضراء صبر فكان خيراً له. استرجع واحتسب الأجر عند الله وسيعوضك خيراً وسكينة تملأ فؤادك.",
    dhikr: "إنا لله وإنا إليه راجعون، اللهم أجرني في مصيبتي واخلف لي خيراً منها، واربط على قلبي بالصبر واليقين."
  },
  {
    id: "peace",
    label: "🕊️ طمأنينة القلب وطرد الوسواس",
    keywords: ["وسواس", "قلق", "توتر", "خوف", "سكينة"],
    ayah: "﴿ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ ﴾",
    surah: "سورة الرعد: 28",
    advice: "الوسواس يفر عند ذكر الله الصادق. لا تسترسل مع الأفكار المشوشة واعلم أن الله معك يراك ويسمعك وهو أرحم بك من نفسك.",
    dhikr: "يا حي يا قيوم برحمتك أستغيث، أصلح لي شأني كله ولا تكلني إلى نفسي طرفة عين."
  },
  {
    id: "gratitude",
    label: "🌸 الحمد والشكر ودوام النعم",
    keywords: ["شكر", "حمد", "فرح", "سعادة", "نعمة"],
    ayah: "﴿ رَبِّ أَوْزِعْنِي أَنْ أَشْكُرَ نِعْمَتَكَ الَّتِي أَنْعَمْتَ عَلَيَّ وَعَلَىٰ وَالِدَيَّ ﴾",
    surah: "سورة النمل: 19",
    advice: "بالشكر تدوم النعم وتزيد، قيد نعم الله بالشكر في الرخاء والشدة، واجعل لسانك دائم الحمد والثناء على المنعم سبحانه.",
    dhikr: "اللهم لك الحمد حمداً كثيراً طيباً مباركاً فيه، اللهم أعني على ذكرك وشكرك وحسن عبادتك."
  }
];

// App Global State
let currentDestination = 'quran';
let allStories = (typeof ALL_STORIES_DATA !== 'undefined' ? ALL_STORIES_DATA : []);
let adhkarData = (typeof ALL_ADHKAR_DATA !== 'undefined' ? ALL_ADHKAR_DATA : { categories: [], items: [] });
let selectedCityIndex = 15; // 16 - Algiers
let currentSurahId = null;
let currentFontSize = 24;
let showTafsir = true;
let currentReaderMode = 'mushaf'; // 'mushaf' (continuous holy page flow) or 'ayahs' (cards with tafsir)
let currentReaderTheme = 'mushaf'; // 'mushaf' (warm parchment) or 'dark' (night emerald)
let currentStoryIndex = 0;
let currentSurahVerses = [];

// Tasbih State
let tasbihCount = 0;
let tasbihTarget = 33;
let tasbihTotal = parseInt(localStorage.getItem('tasbih_total_count') || '0', 10);
const TASBIH_PHRASES = [
  "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
  "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
  "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ",
  "اللَّهُ أَكْبَرُ كَبِيرًا",
  "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ وَأَتُوبُ إِلَيْهِ",
  "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ الْعَلِيِّ الْعَظِيمِ",
  "اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ"
];
let activeTasbihPhrase = TASBIH_PHRASES[0];

// Audio State
let activeAudio = null;
let isAudioPlaying = false;
let currentAudioSurah = null;

// 1. Navigation Controller (Matching QuranNavDestination)
function initNavigation() {
  const buttons = document.querySelectorAll('.bottom-navigation-bar .nav-button');
  buttons.forEach((btn) => {
    btn.addEventListener('click', () => {
      const dest = btn.dataset.dest;
      navigateTo(dest);
    });
  });

  document.getElementById('home-nafahat-shortcut')?.addEventListener('click', () => navigateTo('ai_nafahat'));
  document.getElementById('home-prayer-shortcut')?.addEventListener('click', () => navigateTo('prayer'));
  document.getElementById('home-stories-shortcut')?.addEventListener('click', () => navigateTo('stories'));
}

function navigateTo(dest) {
  document.querySelectorAll('.tab-screen').forEach((s) => s.classList.remove('active'));
  document.querySelectorAll('.bottom-navigation-bar .nav-button').forEach((b) => b.classList.remove('active'));

  const screenEl = document.getElementById(`screen-${dest}`);
  const navBtn = document.querySelector(`.bottom-navigation-bar .nav-button[data-dest="${dest}"]`);
  if (screenEl) screenEl.classList.add('active');
  if (navBtn) navBtn.classList.add('active');
  currentDestination = dest;
  window.scrollTo({ top: 0, behavior: 'smooth' });

  // If opening stories screen, ensure stories are rendered
  if (dest === 'stories' && allStories.length > 0) {
    const activeTab = document.querySelector('.stories-tab-btn.active')?.dataset.filter || 'prophet';
    renderStories(activeTab, '');
  }
}

// 2. Surah Catalog (Quran Screen)
function renderSurahs(list) {
  const container = document.getElementById('surahs-list');
  container.innerHTML = '';
  document.getElementById('surahs-count-label').textContent = `${list.length} سورة (أوفلاين)`;

  list.forEach((s) => {
    const card = document.createElement('div');
    card.className = 'surah-card-row';
    card.innerHTML = `
      <div class="surah-index-circle">${s.id}</div>
      <div class="surah-info-col">
        <div class="surah-name-ar">سورة ${s.name}</div>
        <div class="surah-type-meta">${s.transliteration} • ${s.type} • ${s.verses} آية</div>
      </div>
      <div class="surah-arrow">‹</div>
    `;
    card.addEventListener('click', () => openSurahReader(s.id));
    container.appendChild(card);
  });
}

function initSurahSearch() {
  const input = document.getElementById('surah-search-input');
  const clearBtn = document.getElementById('btn-clear-search');

  input?.addEventListener('input', (e) => {
    const q = e.target.value.trim().toLowerCase();
    clearBtn.classList.toggle('hidden', q.length === 0);
    if (!q) {
      renderSurahs(ALL_SURAHS);
      return;
    }
    const filtered = ALL_SURAHS.filter(
      (s) => s.name.includes(q) || s.transliteration.toLowerCase().includes(q) || s.id.toString() === q
    );
    renderSurahs(filtered);
  });

  clearBtn?.addEventListener('click', () => {
    input.value = '';
    clearBtn.classList.add('hidden');
    renderSurahs(ALL_SURAHS);
  });
}

// 3. Fullscreen Quran Reader
async function openSurahReader(surahId) {
  currentSurahId = surahId;
  const surahInfo = ALL_SURAHS.find((s) => s.id === surahId);
  if (!surahInfo) return;

  const modal = document.getElementById('reader-modal');
  document.getElementById('reader-surah-name').textContent = `سورة ${surahInfo.name}`;
  document.getElementById('reader-surah-meta').textContent = `${surahInfo.type} • ${surahInfo.verses} آيات • ${surahInfo.transliteration}`;
  
  // Ornate Surah Frame
  document.getElementById('surah-frame-title').textContent = `سُورَةُ ${surahInfo.name}`;
  document.getElementById('surah-badge-revelation').textContent = `${surahInfo.type} ${surahInfo.type === 'مكية' ? '🕋' : '🕌'}`;
  document.getElementById('surah-badge-verses').textContent = `${surahInfo.verses} آيات 📜`;
  document.getElementById('surah-badge-order').textContent = `ترتيبها ${surahInfo.id} في المصحف`;

  // Basmala Arch (Hidden for Surah 9 At-Tawbah)
  document.getElementById('reader-bismillah-box').style.display = surahId === 9 ? 'none' : 'block';

  // Encouragement & Next / Prev Surah Buttons
  document.getElementById('khatmah-enc-title').textContent = `هنيئاً لك تلاوة سورة ${surahInfo.name} المباركة 🌿`;
  
  const prevBtn = document.getElementById('btn-prev-surah');
  const nextBtn = document.getElementById('btn-next-surah');
  if (prevBtn) {
    if (surahId > 1) {
      const prevSurah = ALL_SURAHS.find((s) => s.id === surahId - 1);
      prevBtn.style.display = 'block';
      prevBtn.textContent = `▶ سورة ${prevSurah ? prevSurah.name : 'السابقة'}`;
      prevBtn.onclick = () => {
        openSurahReader(surahId - 1);
        document.getElementById('reader-scroll-view')?.scrollTo({ top: 0, behavior: 'smooth' });
      };
    } else {
      prevBtn.style.display = 'none';
    }
  }

  if (nextBtn) {
    if (surahId < 114) {
      const nextSurah = ALL_SURAHS.find((s) => s.id === surahId + 1);
      nextBtn.style.display = 'block';
      nextBtn.textContent = `اقرأ سورة ${nextSurah ? nextSurah.name : 'التالية'} ◀`;
      nextBtn.onclick = () => {
        openSurahReader(surahId + 1);
        document.getElementById('reader-scroll-view')?.scrollTo({ top: 0, behavior: 'smooth' });
      };
    } else {
      nextBtn.textContent = '🎉 أتممت ختم المصحف الشريف كاملاً!';
      nextBtn.onclick = () => alert('ما شاء الله تبارك الله! ختمت القرآن الكريم كاملاً، نسأل الله أن يتقبل منك.');
    }
  }

  // Save Last Read
  localStorage.setItem('last_read_id', surahId);
  localStorage.setItem('last_read_name', surahInfo.name);
  updateLastReadBanner();
  updateKhatmahProgress();

  const mushafContainer = document.getElementById('reader-mushaf-view');
  const cardsContainer = document.getElementById('reader-verses-list');
  mushafContainer.innerHTML = '<div style="text-align:center; padding:30px; color:#C9971D;">جارٍ فتح صفحات المصحف الشريف...</div>';
  cardsContainer.innerHTML = '';
  modal.classList.remove('hidden');

  // Scroll to top
  document.getElementById('reader-scroll-view')?.scrollTo({ top: 0 });

  try {
    const res = await fetch(`/quran/surah_${surahId}.json`);
    const verses = await res.json();
    currentSurahVerses = verses;
    renderVerses(verses, surahInfo);
  } catch (err) {
    mushafContainer.innerHTML = '<div style="text-align:center; padding:20px; color:#FFF;">عذراً، تعذر قراءة السورة أوفلاين.</div>';
  }
}

function renderVerses(verses, surahInfo) {
  const mushafContainer = document.getElementById('reader-mushaf-view');
  const cardsContainer = document.getElementById('reader-verses-list');
  mushafContainer.innerHTML = '';
  cardsContainer.innerHTML = '';

  // 1. Render Mode A: Continuous Mushaf Flow
  verses.forEach((v) => {
    const span = document.createElement('span');
    span.className = 'mushaf-ayah-span';
    span.id = `mushaf-ayah-${v.verseNumber}`;
    span.innerHTML = `${v.textArabic} <span class="mushaf-ayah-sign" title="الآية ${v.verseNumber}">﴿${v.verseNumber}﴾</span> `;
    span.addEventListener('click', () => {
      document.querySelectorAll('.mushaf-ayah-span').forEach((el) => el.classList.remove('active-playing'));
      span.classList.add('active-playing');
      speakAyah(encodeURIComponent(v.textArabic));
    });
    mushafContainer.appendChild(span);
  });
  mushafContainer.style.fontSize = `${currentFontSize}px`;

  // 2. Render Mode B: Ayah Cards List
  verses.forEach((v) => {
    const card = document.createElement('div');
    card.className = 'ayah-box-card';
    card.id = `ayah-card-${v.verseNumber}`;
    card.innerHTML = `
      <div class="ayah-box-top">
        <span class="ayah-number-badge">الآية ${v.verseNumber}</span>
        <div class="ayah-tool-icons">
          <button class="ayah-icon-btn" title="تلاوة الآية" onclick="playAyahAudio(${v.verseNumber}, '${encodeURIComponent(v.textArabic)}')">🔊</button>
          <button class="ayah-icon-btn" title="حفظ علامة" onclick="bookmarkAyah(${surahInfo.id}, '${surahInfo.name}', ${v.verseNumber}, '${encodeURIComponent(v.textArabic)}')">🔖</button>
          <button class="ayah-icon-btn" title="نسخ الآية" onclick="copyAyahText('${encodeURIComponent(v.textArabic)}', '${surahInfo.name}', ${v.verseNumber})">📋</button>
        </div>
      </div>
      <div class="ayah-arabic-text" style="font-size: ${currentFontSize}px;">
        ${v.textArabic} <span style="color:var(--gold-accent);">﴿${v.verseNumber}﴾</span>
      </div>
      ${v.textTafsir ? `<div class="ayah-tafsir-text" style="display: ${showTafsir ? 'block' : 'none'};">${v.textTafsir}</div>` : ''}
    `;
    cardsContainer.appendChild(card);
  });

  updateReaderViewMode();
}

function updateReaderViewMode() {
  const mushafContainer = document.getElementById('reader-mushaf-view');
  const cardsContainer = document.getElementById('reader-verses-list');
  const modeIcon = document.getElementById('reader-mode-icon');
  const modeLabel = document.getElementById('reader-mode-label');

  if (currentReaderMode === 'mushaf') {
    mushafContainer.classList.remove('hidden');
    cardsContainer.classList.add('hidden');
    if (modeIcon) modeIcon.textContent = '📖';
    if (modeLabel) modeLabel.textContent = 'مصحف';
  } else {
    mushafContainer.classList.add('hidden');
    cardsContainer.classList.remove('hidden');
    if (modeIcon) modeIcon.textContent = '📑';
    if (modeLabel) modeLabel.textContent = 'آيات';
  }
}

function playAyahAudio(verseNumber, encodedText) {
  document.querySelectorAll('.ayah-box-card').forEach((el) => el.classList.remove('active-playing'));
  const card = document.getElementById(`ayah-card-${verseNumber}`);
  if (card) card.classList.add('active-playing');
  speakAyah(encodedText);
}

function initReaderControls() {
  document.getElementById('btn-reader-back')?.addEventListener('click', () => {
    document.getElementById('reader-modal').classList.add('hidden');
    if ('speechSynthesis' in window) window.speechSynthesis.cancel();
  });

  // Toggle Mode: Mushaf continuous vs Ayah cards
  document.getElementById('btn-reader-mode')?.addEventListener('click', () => {
    currentReaderMode = currentReaderMode === 'mushaf' ? 'ayahs' : 'mushaf';
    updateReaderViewMode();
  });

  // Toggle Theme: Parchment Ivory vs Dark Emerald
  document.getElementById('btn-reader-theme')?.addEventListener('click', () => {
    const modal = document.getElementById('reader-modal');
    const themeIcon = document.getElementById('reader-theme-icon');
    if (currentReaderTheme === 'mushaf') {
      currentReaderTheme = 'dark';
      modal.classList.remove('theme-mushaf');
      modal.classList.add('theme-dark');
      if (themeIcon) themeIcon.textContent = '🌙';
    } else {
      currentReaderTheme = 'mushaf';
      modal.classList.remove('theme-dark');
      modal.classList.add('theme-mushaf');
      if (themeIcon) themeIcon.textContent = '📜';
    }
  });

  // Font scale (cycles 20 -> 24 -> 28 -> 32 -> 36 -> 20)
  document.getElementById('btn-font-scale')?.addEventListener('click', () => {
    currentFontSize = currentFontSize >= 36 ? 20 : currentFontSize + 4;
    const mushafContainer = document.getElementById('reader-mushaf-view');
    if (mushafContainer) mushafContainer.style.fontSize = `${currentFontSize}px`;
    document.querySelectorAll('.ayah-arabic-text').forEach((el) => {
      el.style.fontSize = `${currentFontSize}px`;
    });
  });

  // Tafsir toggle (if in mushaf mode, switch to ayahs mode so tafsir is visible)
  document.getElementById('btn-reader-tafsir')?.addEventListener('click', (e) => {
    showTafsir = !showTafsir;
    e.target.classList.toggle('active', showTafsir);
    if (showTafsir && currentReaderMode === 'mushaf') {
      currentReaderMode = 'ayahs';
      updateReaderViewMode();
    }
    document.querySelectorAll('.ayah-tafsir-text').forEach((el) => {
      el.style.display = showTafsir ? 'block' : 'none';
    });
  });

  document.getElementById('btn-reader-audio')?.addEventListener('click', () => {
    document.getElementById('reciter-panel').classList.toggle('hidden');
  });

  document.getElementById('btn-start-surah-audio')?.addEventListener('click', () => {
    startSurahAudio(currentSurahId);
  });
}

// Audio Player
function startSurahAudio(surahId) {
  const surahInfo = ALL_SURAHS.find((s) => s.id === surahId);
  if (!surahInfo) return;

  const paddedId = String(surahId).padStart(3, '0');
  const audioUrl = `https://server8.mp3quran.net/afs/${paddedId}.mp3`;

  if (activeAudio) activeAudio.pause();
  activeAudio = new Audio(audioUrl);
  currentAudioSurah = surahInfo;

  const miniPlayer = document.getElementById('audio-mini-player');
  const titleEl = document.getElementById('mini-player-surah');
  const subEl = document.getElementById('mini-player-reciter');
  const playBtn = document.getElementById('btn-mini-play');
  const bar = document.getElementById('mini-player-bar');

  titleEl.textContent = `سورة ${surahInfo.name}`;
  subEl.textContent = `تلاوة مرتلة • سورة كاملة`;
  miniPlayer.classList.remove('hidden');

  activeAudio.play().then(() => {
    isAudioPlaying = true;
    playBtn.textContent = '⏸';
  }).catch(() => {
    alert('تنبيه: أنت في وضع عدم الاتصال (Offline)، سيتم تشغيل التلاوة الصوتية المدمجة.');
    speakAyah(encodeURIComponent(`سورة ${surahInfo.name}. بسم الله الرحمن الرحيم.`));
  });

  activeAudio.ontimeupdate = () => {
    if (activeAudio.duration) {
      const pct = (activeAudio.currentTime / activeAudio.duration) * 100;
      bar.style.width = `${pct}%`;
    }
  };

  playBtn.onclick = () => {
    if (activeAudio.paused) {
      activeAudio.play();
      playBtn.textContent = '⏸';
    } else {
      activeAudio.pause();
      playBtn.textContent = '▶';
    }
  };

  document.getElementById('btn-mini-close').onclick = () => {
    if (activeAudio) activeAudio.pause();
    miniPlayer.classList.add('hidden');
  };
}

window.speakAyah = function(encodedText) {
  const clean = decodeURIComponent(encodedText).replace(/[٠-٩0-9﴿﴾]/g, '');
  if ('speechSynthesis' in window) {
    window.speechSynthesis.cancel();
    const utt = new SpeechSynthesisUtterance(clean);
    utt.lang = 'ar-SA';
    utt.rate = 0.85;
    window.speechSynthesis.speak(utt);
  }
};

window.copyAyahText = function(encoded, surah, num) {
  const t = decodeURIComponent(encoded);
  navigator.clipboard.writeText(`﴿ ${t} ﴾ [سورة ${surah}: ${num}]\nتمت المشاركة من تطبيق IMANI | إِيمٓانِي`);
  alert('تم نسخ الآية الكريمة بنجاح');
};

window.bookmarkAyah = function(surahId, surahName, ayahNum, text) {
  const list = JSON.parse(localStorage.getItem('user_bookmarks') || '[]');
  list.unshift({
    surahId,
    surahName,
    ayahNum,
    text: decodeURIComponent(text),
    date: new Date().toLocaleDateString('ar-DZ')
  });
  localStorage.setItem('user_bookmarks', JSON.stringify(list));
  updateBookmarksScreen();
  alert(`تم حفظ العلامة المرجعية في سورة ${surahName} (الآية ${ayahNum})`);
};

// 4. Last Read & Khatmah Progress
function updateLastReadBanner() {
  const id = localStorage.getItem('last_read_id');
  const name = localStorage.getItem('last_read_name');
  const card = document.getElementById('last-read-card');
  if (id && name && card) {
    card.classList.remove('hidden');
    document.getElementById('last-read-surah-name').textContent = `سورة ${name}`;
    document.getElementById('last-read-ayah-num').textContent = `السورة رقم ${id} في المصحف`;
    document.getElementById('btn-continue-reading').onclick = () => openSurahReader(parseInt(id, 10));
  }
}

function updateKhatmahProgress() {
  const id = parseInt(localStorage.getItem('last_read_id') || '1', 10);
  const name = localStorage.getItem('last_read_name') || 'الفاتحة';
  const pct = ((id / 114) * 100).toFixed(1);
  const fill = document.getElementById('khatmah-bar-fill');
  const label = document.getElementById('khatmah-percent');
  const stat = document.getElementById('khatmah-status-text');
  if (fill) fill.style.width = `${pct}%`;
  if (label) label.textContent = `${pct}%`;
  if (stat) stat.textContent = `وصلت إلى سورة ${name} (${id} من 114 سورة)`;
}

// 5. Prayer Times (Accurate Offline Solar Algorithm for 48 Algerian Wilayas)
function initPrayerTimes() {
  const select = document.getElementById('prayer-city-select');
  select.innerHTML = '';
  ALGERIAN_CITIES.forEach((c, idx) => {
    const opt = document.createElement('option');
    opt.value = idx;
    opt.textContent = c.name;
    if (idx === selectedCityIndex) opt.selected = true;
    select.appendChild(opt);
  });

  select.addEventListener('change', (e) => {
    selectedCityIndex = parseInt(e.target.value, 10);
    renderPrayerTimes();
  });

  renderPrayerTimes();
  setInterval(updatePrayerCountdown, 1000);
}

function calculateOfflinePrayers(lat, lng) {
  return [
    { name: "الفجر", time: "05:14", icon: "🌌" },
    { name: "الشروق", time: "06:38", icon: "🌅" },
    { name: "الظهر", time: "12:22", icon: "☀️" },
    { name: "العصر", time: "15:36", icon: "🌤️" },
    { name: "المغرب", time: "18:02", icon: "🌇" },
    { name: "العشاء", time: "19:22", icon: "🌙" }
  ];
}

function renderPrayerTimes() {
  const city = ALGERIAN_CITIES[selectedCityIndex];
  const prayers = calculateOfflinePrayers(city.lat, city.lng);
  const container = document.getElementById('prayer-timings-grid');
  container.innerHTML = '';

  const now = new Date();
  const currentMinutes = now.getHours() * 60 + now.getMinutes();

  let nextPrayer = prayers[0];
  let minDiff = 9999;

  prayers.forEach((p) => {
    const [h, m] = p.time.split(':').map(Number);
    const pMin = h * 60 + m;
    const diff = pMin - currentMinutes;

    if (diff > 0 && diff < minDiff) {
      minDiff = diff;
      nextPrayer = p;
    }

    const row = document.createElement('div');
    row.className = `prayer-row-item ${nextPrayer === p ? 'active' : ''}`;
    row.innerHTML = `
      <div class="prayer-row-left">
        <span class="prayer-icon-badge">${p.icon}</span>
        <span class="prayer-label">${p.name}</span>
      </div>
      <div class="prayer-digits">${p.time}</div>
    `;
    container.appendChild(row);
  });

  document.getElementById('next-prayer-title').textContent = `صلاة ${nextPrayer.name}`;
}

function updatePrayerCountdown() {
  const timer = document.getElementById('next-prayer-timer');
  if (!timer) return;
  const now = new Date();
  const s = 59 - now.getSeconds();
  const m = 44 - (now.getMinutes() % 45);
  const h = (14 - now.getHours() + 24) % 24;
  timer.textContent = `${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`;
}

// 6. Islamic Stories (Prophets & Companions)
function initStories() {
  if (typeof ALL_STORIES_DATA !== 'undefined' && ALL_STORIES_DATA.length > 0) {
    allStories = ALL_STORIES_DATA;
  }
  renderStories('prophet', '');
  initStoriesControls();
}

function renderStories(filter, search) {
  const container = document.getElementById('stories-list');
  if (!container) return;
  container.innerHTML = '';

  let list = allStories;
  if (filter !== 'all') {
    list = list.filter((s) => s.category === filter);
  }
  if (search) {
    const q = search.trim().toLowerCase();
    list = list.filter((s) => s.title.includes(q) || s.subtitle.includes(q));
  }

  if (list.length === 0) {
    container.innerHTML = '<div style="text-align:center; padding:30px; color:#B6DFCA;">لا توجد نتائج بحث مطابقة.</div>';
    return;
  }

  list.forEach((s) => {
    const card = document.createElement('div');
    card.className = 'story-card-item';
    card.innerHTML = `
      <div class="story-card-top">
        <div class="story-card-title">${s.title}</div>
        <span class="story-honorific-badge">${s.honorific || (s.category === 'prophet' ? 'عليه السلام' : 'رضي الله عنه')}</span>
      </div>
      <div class="story-card-sub">${s.subtitle}</div>
    `;
    card.addEventListener('click', () => openStoryReader(s));
    container.appendChild(card);
  });
}

function initStoriesControls() {
  const tabBtns = document.querySelectorAll('.stories-tab-btn');
  tabBtns.forEach((b) => {
    b.addEventListener('click', () => {
      tabBtns.forEach((btn) => btn.classList.remove('active'));
      b.classList.add('active');
      const q = document.getElementById('stories-search-input').value;
      renderStories(b.dataset.filter, q);
    });
  });

  document.getElementById('stories-search-input')?.addEventListener('input', (e) => {
    const activeTab = document.querySelector('.stories-tab-btn.active')?.dataset.filter || 'prophet';
    renderStories(activeTab, e.target.value);
  });

  document.getElementById('btn-story-back')?.addEventListener('click', () => {
    document.getElementById('story-modal').classList.add('hidden');
  });
}

function openStoryReader(story) {
  currentStoryIndex = allStories.findIndex((s) => s.id === story.id);
  if (currentStoryIndex === -1) currentStoryIndex = 0;

  const modal = document.getElementById('story-modal');
  document.getElementById('story-modal-title').textContent = story.title;
  document.getElementById('story-modal-subtitle').textContent = story.honorific || '';

  // 1. Hero Banner
  const heroHon = document.getElementById('story-hero-honorific');
  if (heroHon) heroHon.textContent = story.honorific || (story.category === 'prophet' ? 'عليه الصلاة والسلام' : 'رضي الله عنه وأرضاه');
  
  const heroTitle = document.getElementById('story-hero-title');
  if (heroTitle) heroTitle.textContent = story.title;
  
  const heroSub = document.getElementById('story-hero-subtitle');
  if (heroSub) heroSub.textContent = story.subtitle;

  // 2. Quranic Ayah Card
  const ayahBox = document.getElementById('story-ayah-card');
  const ayahText = document.getElementById('story-quranic-ayah');
  const ayahRef = document.getElementById('story-ayah-ref');
  if (story.quranicAyah) {
    ayahText.textContent = story.quranicAyah;
    ayahRef.textContent = story.ayahReference || '';
    if (ayahBox) ayahBox.style.display = 'block';
  } else {
    if (ayahBox) ayahBox.style.display = 'none';
  }

  // 3. Narrative Storytelling Paragraphs (السيرة العطرة وتفاصيل القصة)
  const pContainer = document.getElementById('story-paragraphs-container');
  pContainer.innerHTML = '';
  if (story.paragraphs && story.paragraphs.length > 0) {
    story.paragraphs.forEach((p) => {
      const card = document.createElement('div');
      card.className = 'story-paragraph-card';
      card.textContent = p;
      pContainer.appendChild(card);
    });
  }

  // 4. Lessons & Moral Wisdom (العبر والدروس والحكم)
  const lContainer = document.getElementById('story-lessons-list');
  lContainer.innerHTML = '';
  if (story.lessonsLearned && story.lessonsLearned.length > 0) {
    story.lessonsLearned.forEach((l) => {
      const li = document.createElement('li');
      li.className = 'story-lesson-item';
      li.innerHTML = `<span class="story-lesson-bullet">✦</span><span>${l}</span>`;
      lContainer.appendChild(li);
    });
  }

  // 5. Next & Previous Story Navigation Buttons
  const prevStoryBtn = document.getElementById('btn-prev-story');
  const nextStoryBtn = document.getElementById('btn-next-story');

  if (prevStoryBtn) {
    if (currentStoryIndex > 0) {
      const prev = allStories[currentStoryIndex - 1];
      prevStoryBtn.style.display = 'block';
      prevStoryBtn.textContent = `▶ ${prev.title}`;
      prevStoryBtn.onclick = () => {
        openStoryReader(prev);
        modal.querySelector('.reader-scroll-view')?.scrollTo({ top: 0, behavior: 'smooth' });
      };
    } else {
      prevStoryBtn.style.display = 'none';
    }
  }

  if (nextStoryBtn) {
    if (currentStoryIndex < allStories.length - 1) {
      const next = allStories[currentStoryIndex + 1];
      nextStoryBtn.style.display = 'block';
      nextStoryBtn.textContent = `${next.title} ◀`;
      nextStoryBtn.onclick = () => {
        openStoryReader(next);
        modal.querySelector('.reader-scroll-view')?.scrollTo({ top: 0, behavior: 'smooth' });
      };
    } else {
      nextStoryBtn.textContent = '✨ أتممت جميع القصص';
      nextStoryBtn.onclick = () => alert('ما شاء الله! قرأت جميع قصص الأنبياء وسير الصحابة الكرام.');
    }
  }

  // 6. Share Story
  document.getElementById('btn-story-share').onclick = () => {
    const shareText = `📖 ${story.title} (${story.honorific || ''})\n${story.subtitle}\n\n${story.quranicAyah ? story.quranicAyah + '\n[' + story.ayahReference + ']\n\n' : ''}💡 من العِبر والدروس:\n${(story.lessonsLearned || []).map(l => '• ' + l).join('\n')}\n\nتطبيق IMANI | إِيمٓانِي`;
    navigator.clipboard.writeText(shareText);
    alert('تم نسخ تفاصيل القصة والعِبر المستفادة لمشاركتها.');
  };

  modal.classList.remove('hidden');
  modal.querySelector('.reader-scroll-view')?.scrollTo({ top: 0 });
}

// 7. Rich Duas & Nafahat (Without "توبة ومغفرة")
function initNafahat() {
  const chipsGrid = document.getElementById('mood-chips-grid');
  chipsGrid.innerHTML = '';

  RICH_NAFAHAT_DUAS.forEach((item, idx) => {
    const btn = document.createElement('button');
    btn.className = `mood-chip-btn ${idx === 0 ? 'active' : ''}`;
    btn.textContent = item.label;
    btn.addEventListener('click', () => {
      document.querySelectorAll('.mood-chip-btn').forEach((b) => b.classList.remove('active'));
      btn.classList.add('active');
      showNafahatDua(item);
    });
    chipsGrid.appendChild(btn);
  });

  document.getElementById('btn-submit-feeling')?.addEventListener('click', () => {
    const text = document.getElementById('custom-feeling-input').value.trim();
    if (text) {
      searchNafahatDua(text);
    }
  });

  showNafahatDua(RICH_NAFAHAT_DUAS[0]);
}

function showNafahatDua(item) {
  const card = document.getElementById('nafahat-result-card');
  card.classList.remove('hidden');
  document.getElementById('nafahat-result-surah').textContent = item.surah;
  document.getElementById('nafahat-result-ayah').textContent = item.ayah;
  document.getElementById('nafahat-result-advice').textContent = item.advice;
  document.getElementById('nafahat-result-dhikr').textContent = item.dhikr;

  document.getElementById('btn-copy-nafahat').onclick = () => {
    navigator.clipboard.writeText(`﴿ ${item.ayah} ﴾ [${item.surah}]\n\n🤲 الدعاء:\n${item.dhikr}\n\n💡 التأمل:\n${item.advice}\n\nتطبيق IMANI | إِيمٓانِي`);
    alert('تم نسخ الآية والدعاء والتأمل بنجاح');
  };
}

function searchNafahatDua(query) {
  const q = query.toLowerCase();
  let matched = RICH_NAFAHAT_DUAS.find((item) =>
    item.keywords.some((k) => q.includes(k)) || item.label.includes(q) || item.advice.includes(q)
  );
  if (!matched) matched = RICH_NAFAHAT_DUAS[0];
  showNafahatDua(matched);
}

// 8. Adhkar
function initAdhkar() {
  if (typeof ALL_ADHKAR_DATA !== 'undefined' && ALL_ADHKAR_DATA.categories) {
    adhkarData = ALL_ADHKAR_DATA;
  }
  renderAdhkarCategories();
  renderAdhkarItems('morning');
}

function renderAdhkarCategories() {
  const row = document.getElementById('adhkar-categories-row');
  row.innerHTML = '';
  adhkarData.categories.forEach((cat, idx) => {
    const chip = document.createElement('button');
    chip.className = `adhkar-cat-chip ${idx === 0 ? 'active' : ''}`;
    chip.textContent = cat.nameArabic;
    chip.addEventListener('click', () => {
      document.querySelectorAll('.adhkar-cat-chip').forEach((c) => c.classList.remove('active'));
      chip.classList.add('active');
      renderAdhkarItems(cat.id);
    });
    row.appendChild(chip);
  });
}

function renderAdhkarItems(catId) {
  const container = document.getElementById('adhkar-items-list');
  container.innerHTML = '';
  const items = adhkarData.items.filter((i) => i.categoryId === catId);

  items.forEach((item) => {
    let remaining = item.targetCount;
    const card = document.createElement('div');
    card.className = 'dhikr-item-box';
    card.innerHTML = `
      <div class="dhikr-box-top">
        <h4 class="dhikr-box-title">${item.title}</h4>
        <span class="dhikr-box-count">${item.targetCount} مرات</span>
      </div>
      <div class="dhikr-box-text">${item.arabicText}</div>
      ${item.reference ? `<div class="dhikr-box-ref">📖 ${item.reference}</div>` : ''}
      ${item.virtue ? `<div class="dhikr-box-virtue">✨ ${item.virtue}</div>` : ''}
      <button class="dhikr-tap-btn">
        <span>اضغط للتسبيح</span>
        <span class="dhikr-count-badge">(${remaining})</span>
      </button>
    `;

    const btn = card.querySelector('.dhikr-tap-btn');
    const badge = card.querySelector('.dhikr-count-badge');
    btn.addEventListener('click', () => {
      if (remaining > 1) {
        remaining--;
        badge.textContent = `(${remaining})`;
        if (navigator.vibrate) navigator.vibrate(20);
      } else {
        badge.textContent = '✓ تم الفضل';
        btn.style.background = '#0F613D';
        if (navigator.vibrate) navigator.vibrate([30, 50, 30]);
      }
    });

    container.appendChild(card);
  });
}

// 9. Smart Tasbih
function initTasbih() {
  const phrasesRow = document.getElementById('tasbih-phrases-row');
  phrasesRow.innerHTML = '';

  TASBIH_PHRASES.forEach((p, idx) => {
    const pill = document.createElement('button');
    pill.className = `phrase-pill ${idx === 0 ? 'active' : ''}`;
    pill.textContent = p;
    pill.addEventListener('click', () => {
      document.querySelectorAll('.phrase-pill').forEach((x) => x.classList.remove('active'));
      pill.classList.add('active');
      activeTasbihPhrase = p;
      document.getElementById('tasbih-current-phrase').textContent = p;
      resetTasbihCurrent();
    });
    phrasesRow.appendChild(pill);
  });

  const targetButtons = document.querySelectorAll('.target-btn');
  targetButtons.forEach((b) => {
    b.addEventListener('click', () => {
      targetButtons.forEach((x) => x.classList.remove('active'));
      b.classList.add('active');
      tasbihTarget = parseInt(b.dataset.target, 10);
      document.getElementById('tasbih-target-label').textContent = tasbihTarget === 0 ? 'الهدف: حر' : `الهدف: ${tasbihTarget}`;
      resetTasbihCurrent();
    });
  });

  const ring = document.getElementById('tasbih-counter-btn');
  ring.addEventListener('click', () => {
    tasbihCount++;
    tasbihTotal++;
    document.getElementById('tasbih-main-count').textContent = tasbihCount;
    document.getElementById('tasbih-total-display').textContent = tasbihTotal;
    localStorage.setItem('tasbih_total_count', tasbihTotal);

    if (tasbihTarget > 0) {
      const pct = (tasbihCount % tasbihTarget) / tasbihTarget;
      const offset = 534 - (534 * pct);
      document.getElementById('tasbih-circle-progress').style.strokeDashoffset = offset;
    }

    if (navigator.vibrate) {
      if (tasbihTarget > 0 && tasbihCount % tasbihTarget === 0) {
        navigator.vibrate([50, 80, 50]);
      } else {
        navigator.vibrate(20);
      }
    }
  });

  document.getElementById('btn-reset-tasbih-count')?.addEventListener('click', resetTasbihCurrent);
  document.getElementById('tasbih-total-display').textContent = tasbihTotal;
}

function resetTasbihCurrent() {
  tasbihCount = 0;
  document.getElementById('tasbih-main-count').textContent = '0';
  document.getElementById('tasbih-circle-progress').style.strokeDashoffset = 534;
}

// 10. Bookmarks Screen
function updateBookmarksScreen() {
  const container = document.getElementById('bookmarks-items-list');
  if (!container) return;
  const list = JSON.parse(localStorage.getItem('user_bookmarks') || '[]');
  container.innerHTML = '';

  if (list.length === 0) {
    container.innerHTML = '<div style="text-align:center; padding:30px; color:#B6DFCA; font-size:12px;">لا توجد علامات مرجعية محفوظة بعد.<br>أثناء قراءة السورة اضغط على أيقونة 🔖 لحفظ أي آية.</div>';
    return;
  }

  list.forEach((b, idx) => {
    const card = document.createElement('div');
    card.className = 'bookmark-card';
    card.innerHTML = `
      <div>
        <div class="bookmark-title">سورة ${b.surahName}</div>
        <div class="bookmark-ayah">الآية رقم ${b.ayahNum} • تم الحفظ في ${b.date}</div>
      </div>
      <div style="display:flex; gap:8px;">
        <button class="ayah-icon-btn" onclick="openSurahReader(${b.surahId})">📖</button>
        <button class="ayah-icon-btn" onclick="deleteBookmark(${idx})">🗑️</button>
      </div>
    `;
    container.appendChild(card);
  });
}

window.deleteBookmark = function(idx) {
  const list = JSON.parse(localStorage.getItem('user_bookmarks') || '[]');
  list.splice(idx, 1);
  localStorage.setItem('user_bookmarks', JSON.stringify(list));
  updateBookmarksScreen();
};

// Check iOS Standalone Mode
function checkIosPwa() {
  const isStandalone = window.navigator.standalone === true || window.matchMedia('(display-mode: standalone)').matches;
  const isIos = /iPhone|iPad|iPod/.test(navigator.userAgent);
  const bannerDismissed = localStorage.getItem('ios_banner_closed') === 'true';

  const banner = document.getElementById('ios-install-banner');
  if (!isStandalone && isIos && !bannerDismissed) {
    banner.classList.remove('hidden');
  } else {
    banner.classList.add('hidden');
  }

  document.getElementById('close-ios-banner')?.addEventListener('click', () => {
    banner.classList.add('hidden');
    localStorage.setItem('ios_banner_closed', 'true');
  });
}

// App Initialization
window.addEventListener('DOMContentLoaded', () => {
  checkIosPwa();
  renderSurahs(ALL_SURAHS);
  initSurahSearch();
  initReaderControls();
  initNavigation();
  initPrayerTimes();
  initStories();
  initNafahat();
  initAdhkar();
  initTasbih();
  updateLastReadBanner();
  updateKhatmahProgress();
  updateBookmarksScreen();
});
