package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranDao {
    // Bookmarks
    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmarkById(id: Int)

    @Query("DELETE FROM bookmarks WHERE surahId = :surahId AND ayahNumber = :ayahNumber")
    suspend fun deleteBookmark(surahId: Int, ayahNumber: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE surahId = :surahId AND ayahNumber = :ayahNumber)")
    fun isBookmarked(surahId: Int, ayahNumber: Int): Flow<Boolean>

    // Last Read
    @Query("SELECT * FROM last_read WHERE id = 1 LIMIT 1")
    fun getLastRead(): Flow<LastReadEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setLastRead(lastRead: LastReadEntity)

    // Favorite Duas
    @Query("SELECT * FROM favorite_duas ORDER BY timestamp DESC")
    fun getFavoriteDuas(): Flow<List<FavoriteDuaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteDua(dua: FavoriteDuaEntity)

    @Query("DELETE FROM favorite_duas WHERE duaId = :duaId")
    suspend fun removeFavoriteDua(duaId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_duas WHERE duaId = :duaId)")
    fun isDuaFavorite(duaId: String): Flow<Boolean>

    // Tasbih Stats
    @Query("SELECT * FROM tasbih_stats")
    fun getTasbihStats(): Flow<List<TasbihCountEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTasbihCount(stats: TasbihCountEntity)
}
