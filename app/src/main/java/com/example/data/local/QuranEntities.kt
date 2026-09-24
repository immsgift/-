package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val surahId: Int,
    val surahName: String,
    val ayahNumber: Int,
    val ayahText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String = ""
)

@Entity(tableName = "last_read")
data class LastReadEntity(
    @PrimaryKey val id: Int = 1,
    val surahId: Int,
    val surahName: String,
    val ayahNumber: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorite_duas")
data class FavoriteDuaEntity(
    @PrimaryKey val duaId: String,
    val title: String,
    val categoryId: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasbih_stats")
data class TasbihCountEntity(
    @PrimaryKey val dhikrKey: String,
    val count: Int,
    val lastUpdated: Long = System.currentTimeMillis()
)
