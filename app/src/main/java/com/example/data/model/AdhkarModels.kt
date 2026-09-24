package com.example.data.model

data class DhikrCategory(
    val id: String,
    val titleArabic: String,
    val titleEnglish: String,
    val iconName: String,
    val isOccasion: Boolean = false,
    val countHint: String = ""
)

data class DhikrItem(
    val id: String,
    val categoryId: String,
    val title: String,
    val arabicText: String,
    val translation: String = "",
    val reference: String = "",
    val virtue: String = "",
    val targetCount: Int = 1,
    val audioUrl: String = ""
)
