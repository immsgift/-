package com.example.data.model

enum class StoryCategory(val titleArabic: String) {
    PROPHET("قصص الأنبياء"),
    SAHABA("سير الصحابة")
}

data class IslamicStory(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: StoryCategory,
    val honorific: String,
    val quranicAyah: String,
    val ayahReference: String,
    val paragraphs: List<String>,
    val lessonsLearned: List<String>
)
