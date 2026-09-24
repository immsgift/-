package com.example.data.model

data class SurahInfo(
    val id: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val englishMeaning: String,
    val versesCount: Int,
    val revelationType: RevelationType,
    val juz: Int,
    val pageNumber: Int
)

enum class RevelationType(val arabicLabel: String) {
    MECCAN("مكية"),
    MEDINAN("مدنية")
}

data class Ayah(
    val id: Int,
    val surahId: Int,
    val verseNumber: Int,
    val textArabic: String,
    val textTranslation: String = "",
    val textTafsir: String = "",
    val audioUrl: String = ""
)

data class Reciter(
    val id: String,
    val nameArabic: String,
    val nameEnglish: String,
    val baseUrl: String
)

val AvailableReciters = listOf(
    Reciter("alafasy", "مشاري راشد العفاسي", "Mishary Rashid Alafasy", "Alafasy_128kbps"),
    Reciter("abdulbasit", "عبد الباسط عبد الصمد (مرتل)", "Abdul Basit (Murattal)", "Abdul_Basit_Murattal_192kbps"),
    Reciter("husary", "محمود خليل الحصري", "Mahmoud Khalil Al-Husary", "Husary_128kbps"),
    Reciter("ghamadi", "سعد الغامدي", "Saad Al-Ghamdi", "Ghamadi_40kbps")
)
