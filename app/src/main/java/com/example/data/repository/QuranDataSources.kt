package com.example.data.repository

import android.content.Context
import com.example.data.model.Ayah
import com.example.data.model.RevelationType
import com.example.data.model.SurahInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

object QuranDataSources {

    // Thread-safe in-memory cache for fast instant reopening
    private val surahVersesCache = ConcurrentHashMap<Int, List<Ayah>>()

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
        SurahInfo(42, "الشورى", "Ash-Shura", "Consultation", 53, RevelationType.MECCAN, 25, 483),
        SurahInfo(43, "الزخرف", "Az-Zukhruf", "The Gold Adornment", 89, RevelationType.MECCAN, 25, 489),
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
        SurahInfo(60, "الممتحنة", "Al-Mumtahanah", "She That Is To Be Examined", 13, RevelationType.MEDINAN, 28, 549),
        SurahInfo(61, "الصف", "As-Saf", "The Ranks", 14, RevelationType.MEDINAN, 28, 551),
        SurahInfo(62, "الجمعة", "Al-Jumu'ah", "Friday", 11, RevelationType.MEDINAN, 28, 553),
        SurahInfo(63, "المنافقون", "Al-Munafiqun", "The Hypocrites", 11, RevelationType.MEDINAN, 28, 554),
        SurahInfo(64, "التغابن", "At-Taghabun", "Mutual Disillusion", 18, RevelationType.MEDINAN, 28, 556),
        SurahInfo(65, "الطلاق", "At-Talaq", "Divorce", 12, RevelationType.MEDINAN, 28, 558),
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
        SurahInfo(76, "الإنسان", "Al-Insan", "Man", 31, RevelationType.MEDINAN, 29, 578),
        SurahInfo(77, "المرسلات", "Al-Mursalat", "The Emissaries", 50, RevelationType.MECCAN, 29, 580),
        SurahInfo(78, "النبأ", "An-Naba", "The Tidings", 40, RevelationType.MECCAN, 30, 582),
        SurahInfo(79, "النازعات", "An-Nazi'at", "Those Who Drag Forth", 46, RevelationType.MECCAN, 30, 583),
        SurahInfo(80, "عبس", "'Abasa", "He Frowned", 42, RevelationType.MECCAN, 30, 585),
        SurahInfo(81, "التكوير", "At-Takwir", "The Overthrowing", 29, RevelationType.MECCAN, 30, 586),
        SurahInfo(82, "الانفطار", "Al-Infitar", "The Cleaving", 19, RevelationType.MECCAN, 30, 587),
        SurahInfo(83, "المطففين", "Al-Mutaffifin", "Defrauding", 36, RevelationType.MECCAN, 30, 587),
        SurahInfo(84, "الانشقاق", "Al-Inshiqaq", "The Splitting Open", 25, RevelationType.MECCAN, 30, 589),
        SurahInfo(85, "البروج", "Al-Buruj", "The Constellations", 22, RevelationType.MECCAN, 30, 590),
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
        SurahInfo(102, "التكاثر", "At-Takathur", "Rivalry in World Increase", 8, RevelationType.MECCAN, 30, 600),
        SurahInfo(103, "العصر", "Al-'Asr", "The Declining Day", 3, RevelationType.MECCAN, 30, 601),
        SurahInfo(104, "الهمزة", "Al-Humazah", "The Traducer", 9, RevelationType.MECCAN, 30, 601),
        SurahInfo(105, "الفيل", "Al-Fil", "The Elephant", 5, RevelationType.MECCAN, 30, 601),
        SurahInfo(106, "قريش", "Quraysh", "Quraysh", 4, RevelationType.MECCAN, 30, 602),
        SurahInfo(107, "الماعون", "Al-Ma'un", "Small Kindnesses", 7, RevelationType.MECCAN, 30, 602),
        SurahInfo(108, "الكوثر", "Al-Kawthar", "Abundance", 3, RevelationType.MECCAN, 30, 602),
        SurahInfo(109, "الكافرون", "Al-Kafirun", "The Disbelievers", 6, RevelationType.MECCAN, 30, 603),
        SurahInfo(110, "النصر", "An-Nasr", "Divine Support", 3, RevelationType.MEDINAN, 30, 603),
        SurahInfo(111, "المسد", "Al-Masad", "Palm Fibre", 5, RevelationType.MECCAN, 30, 603),
        SurahInfo(112, "الإخلاص", "Al-Ikhlas", "Sincerity", 4, RevelationType.MECCAN, 30, 604),
        SurahInfo(113, "الفلق", "Al-Falaq", "The Daybreak", 5, RevelationType.MECCAN, 30, 604),
        SurahInfo(114, "الناس", "An-Nas", "Mankind", 6, RevelationType.MECCAN, 30, 604)
    )

    // Daily Featured Ayah list with inspiring reflections
    val dailyAyat = listOf(
        Pair(
            Ayah(1, 2, 186, "وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ ۖ أُجِيبُ دَعْوَةَ الدَّاعِ إِذَا دَعَانِ ۖ فَلْيَسْتَجِيبُوا لِي وَلْيُؤْمِنُوا بِي لَعَلَّهُمْ يَرْشُدُونَ", "And when My servants ask you concerning Me, indeed I am near. I respond to the invocation of the supplicant when he calls upon Me.", "الله قريب منك دائماً يسمع دعاءك وسؤالك، فلا تيأس وادعه بيقين وإخلاص."),
            "الله قريب منك دائماً، يسمع نجواك ويستجيب لدعائك فأكثر من التضرع واليقين بالإجابة."
        ),
        Pair(
            Ayah(2, 65, 3, "وَيَرْزُقْهُ مِنْ حَيْثُ لَا يَحْتَسِبُ ۚ وَمَن يَتَوَكَّلْ عَلَى اللَّهِ فَهُوَ حَسْبُهُ ۚ إِنَّ اللَّهَ بَالِغُ أَمْرِهِ ۚ قَدْ جَعَلَ اللَّهُ لِكُلِّ شَيْءٍ قَدْرًا", "And whoever relies upon Allah - then He is sufficient for him. Indeed, Allah will accomplish His purpose.", "التوكل على الله كافٍ لجلب الرزق وتفريج الكروب وتيسير الأمور المقدرة."),
            "التوكل على الله هو مفتاح السكينة وراحة البال؛ فمن فوض أمره لربه كفاه ما أهمه."
        ),
        Pair(
            Ayah(3, 94, 6, "إِنَّ مَعَ الْعُسْرِ يُسْرًا", "Indeed, with hardship [will be] ease.", "بشارة ربانية مؤكدة بأن بعد كل شدة يسرين وفرجاً قريباً يبهج القلب."),
            "لا يدوم ضيق ولا يطول كرب، فمع كل شدة يأتي اليسر والفرج من حيث لا تحتسب."
        )
    )

    /**
     * Primary Source of Truth (100% OFFLINE SUPPORT FOR ALL 114 SURAHS):
     * 1. Checks in-memory cache for zero-latency response.
     * 2. Reads the official Tanzil/Uthmani + Tafsir Al-Muyassar JSON from app assets.
     * 3. Falls back to online API if needed.
     */
    suspend fun getSurahVerses(context: Context, surahId: Int): List<Ayah> = withContext(Dispatchers.IO) {
        // 1. Check in-memory cache
        surahVersesCache[surahId]?.let { return@withContext it }

        // 2. Load directly from 100% authentic offline bundled assets (All 114 Surahs available!)
        val offlineList = loadSurahFromAssets(context, surahId)
        if (!offlineList.isNullOrEmpty()) {
            surahVersesCache[surahId] = offlineList
            return@withContext offlineList
        }

        // 3. Fallback to API if assets unavailable
        val fetchedList = fetchFromAlquranCloud(surahId)
        if (!fetchedList.isNullOrEmpty()) {
            surahVersesCache[surahId] = fetchedList
            return@withContext fetchedList
        }

        emptyList()
    }

    /**
     * Reads the pre-packaged authentic JSON from /assets/quran/surah_{surahId}.json
     */
    private fun loadSurahFromAssets(context: Context, surahId: Int): List<Ayah>? {
        return try {
            val assetPath = "quran/surah_$surahId.json"
            context.assets.open(assetPath).use { inputStream ->
                InputStreamReader(inputStream, "UTF-8").use { reader ->
                    val jsonStr = reader.readText()
                    val jsonArray = JSONArray(jsonStr)
                    val list = ArrayList<Ayah>(jsonArray.length())
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        list.add(
                            Ayah(
                                id = obj.getInt("id"),
                                surahId = obj.getInt("surahId"),
                                verseNumber = obj.getInt("verseNumber"),
                                textArabic = obj.getString("textArabic"),
                                textTranslation = obj.optString("textTranslation", ""),
                                textTafsir = obj.optString("textTafsir", "")
                            )
                        )
                    }
                    list
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("QuranDataSources", "Error loading asset for surah $surahId: ${e.message}", e)
            null
        }
    }

    /**
     * Optional network fallback
     */
    private fun fetchFromAlquranCloud(surahId: Int): List<Ayah>? {
        return try {
            val endpoint = "https://api.alquran.cloud/v1/surah/$surahId/editions/quran-uthmani,ar.muyassar,en.sahih"
            val url = URL(endpoint)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 4000
                readTimeout = 4000
                requestMethod = "GET"
                setRequestProperty("Accept", "application/json")
            }

            if (connection.responseCode == 200) {
                val json = connection.inputStream.bufferedReader().use { it.readText() }
                val root = JSONObject(json)
                if (root.optInt("code", 0) == 200) {
                    val data = root.getJSONArray("data")
                    val uthmaniEdition = data.getJSONObject(0)
                    val tafsirEdition = if (data.length() > 1) data.getJSONObject(1) else null
                    val englishEdition = if (data.length() > 2) data.getJSONObject(2) else null

                    val uthmaniAyahs = uthmaniEdition.getJSONArray("ayahs")
                    val tafsirAyahs = tafsirEdition?.optJSONArray("ayahs")
                    val englishAyahs = englishEdition?.optJSONArray("ayahs")

                    val list = mutableListOf<Ayah>()
                    for (i in 0 until uthmaniAyahs.length()) {
                        val aObj = uthmaniAyahs.getJSONObject(i)
                        val numInSurah = aObj.getInt("numberInSurah")
                        val textArabic = aObj.getString("text")
                        val tafsirText = tafsirAyahs?.optJSONObject(i)?.optString("text") ?: ""
                        val engText = englishAyahs?.optJSONObject(i)?.optString("text") ?: ""

                        list.add(
                            Ayah(
                                id = aObj.getInt("number"),
                                surahId = surahId,
                                verseNumber = numInSurah,
                                textArabic = textArabic,
                                textTranslation = engText,
                                textTafsir = tafsirText
                            )
                        )
                    }
                    if (list.isNotEmpty()) {
                        return list
                    }
                }
            }
            null
        } catch (_: Exception) {
            null
        }
    }
}
