package com.example.data.repository

import com.example.data.local.BookmarkEntity
import com.example.data.local.FavoriteDuaEntity
import com.example.data.local.LastReadEntity
import com.example.data.local.QuranDao
import com.example.data.local.TasbihCountEntity
import com.example.data.model.Ayah
import com.example.data.model.DhikrCategory
import com.example.data.model.DhikrItem
import com.example.data.model.SurahInfo
import kotlinx.coroutines.flow.Flow

class QuranRepository(private val quranDao: QuranDao) {

    // Surah list
    fun getAllSurahs(): List<SurahInfo> = QuranDataSources.allSurahs

    fun getSurahById(id: Int): SurahInfo? = QuranDataSources.allSurahs.firstOrNull { it.id == id }

    suspend fun getSurahVerses(surahId: Int): List<Ayah> = QuranDataSources.getSurahVerses(surahId)

    // Adhkar & Duas
    fun getCategories(): List<DhikrCategory> = AdhkarDataSources.categories

    fun getAdhkarByCategory(categoryId: String): List<DhikrItem> =
        AdhkarDataSources.allAdhkarItems.filter { it.categoryId == categoryId }

    fun getAllAdhkarAndDuas(): List<DhikrItem> = AdhkarDataSources.allAdhkarItems

    // Bookmarks
    val bookmarks: Flow<List<BookmarkEntity>> = quranDao.getAllBookmarks()

    suspend fun addBookmark(surahId: Int, surahName: String, ayahNumber: Int, ayahText: String) {
        quranDao.insertBookmark(
            BookmarkEntity(
                surahId = surahId,
                surahName = surahName,
                ayahNumber = ayahNumber,
                ayahText = ayahText
            )
        )
    }

    suspend fun removeBookmark(id: Int) = quranDao.deleteBookmarkById(id)

    suspend fun removeBookmarkBySurahAyah(surahId: Int, ayahNumber: Int) =
        quranDao.deleteBookmark(surahId, ayahNumber)

    fun isBookmarked(surahId: Int, ayahNumber: Int): Flow<Boolean> =
        quranDao.isBookmarked(surahId, ayahNumber)

    // Last Read
    val lastRead: Flow<LastReadEntity?> = quranDao.getLastRead()

    suspend fun setLastRead(surahId: Int, surahName: String, ayahNumber: Int) {
        quranDao.setLastRead(
            LastReadEntity(
                id = 1,
                surahId = surahId,
                surahName = surahName,
                ayahNumber = ayahNumber
            )
        )
    }

    // Favorite Duas
    val favoriteDuas: Flow<List<FavoriteDuaEntity>> = quranDao.getFavoriteDuas()

    suspend fun toggleFavoriteDua(duaId: String, title: String, categoryId: String, isCurrentlyFav: Boolean) {
        if (isCurrentlyFav) {
            quranDao.removeFavoriteDua(duaId)
        } else {
            quranDao.addFavoriteDua(FavoriteDuaEntity(duaId = duaId, title = title, categoryId = categoryId))
        }
    }

    fun isDuaFavorite(duaId: String): Flow<Boolean> = quranDao.isDuaFavorite(duaId)

    // Tasbih
    val tasbihStats: Flow<List<TasbihCountEntity>> = quranDao.getTasbihStats()

    suspend fun updateTasbih(dhikrKey: String, count: Int) {
        quranDao.updateTasbihCount(
            TasbihCountEntity(
                dhikrKey = dhikrKey,
                count = count,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }
}
