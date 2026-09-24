package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioPlaybackState
import com.example.audio.QuranAudioManager
import com.example.data.local.BookmarkEntity
import com.example.data.local.FavoriteDuaEntity
import com.example.data.local.LastReadEntity
import com.example.data.local.QuranDatabase
import com.example.data.model.AvailableReciters
import com.example.data.model.Ayah
import com.example.data.model.DhikrCategory
import com.example.data.model.DhikrItem
import com.example.data.model.Reciter
import com.example.data.model.SurahInfo
import com.example.data.repository.AdhkarDataSources
import com.example.data.repository.QuranDataSources
import com.example.data.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class QuranUiState(
    val searchQuery: String = "",
    val activeSurah: SurahInfo? = null,
    val activeVerses: List<Ayah> = emptyList(),
    val isLoadingVerses: Boolean = false,
    val fontSizeSp: Float = 24f,
    val showTranslation: Boolean = true,
    val showTafsir: Boolean = false,
    val selectedReciter: Reciter = AvailableReciters[0]
)

data class AdhkarUiState(
    val selectedCategoryId: String = "morning",
    val filterType: AdhkarFilter = AdhkarFilter.ALL,
    val searchQuery: String = "",
    val dhikrCounts: Map<String, Int> = emptyMap(),
    val isAutoReciting: Boolean = false
)

enum class AdhkarFilter(val label: String) {
    ALL("الكل"),
    DAILY("الأذكار اليومية"),
    OCCASIONS("أدعية المناسبات")
}

data class TasbihUiState(
    val currentCount: Int = 0,
    val targetCount: Int = 33, // 33, 100, or 0 (Free)
    val selectedDhikr: String = "سُبْحَانَ اللَّهِ",
    val lapCount: Int = 0,
    val totalCount: Int = 0,
    val isVibrationEnabled: Boolean = true
)

val PresetTasbihPhrases = listOf(
    "سُبْحَانَ اللَّهِ",
    "الْحَمْدُ لِلَّهِ",
    "لَا إِلَٰهَ إِلَّا اللَّهُ",
    "اللَّهُ أَكْبَرُ",
    "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ",
    "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
    "اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ",
    "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ ، سُبْحَانَ اللَّهِ الْعَظِيمِ"
)

class QuranViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuranRepository
    val audioManager: QuranAudioManager

    private val _quranState = MutableStateFlow(QuranUiState())
    val quranState: StateFlow<QuranUiState> = _quranState.asStateFlow()

    private val _adhkarState = MutableStateFlow(AdhkarUiState())
    val adhkarState: StateFlow<AdhkarUiState> = _adhkarState.asStateFlow()

    private val _tasbihState = MutableStateFlow(TasbihUiState())
    val tasbihState: StateFlow<TasbihUiState> = _tasbihState.asStateFlow()

    val audioPlaybackState: StateFlow<AudioPlaybackState>

    val bookmarks: StateFlow<List<BookmarkEntity>>
    val lastRead: StateFlow<LastReadEntity?>
    val favoriteDuas: StateFlow<List<FavoriteDuaEntity>>

    init {
        val db = QuranDatabase.getDatabase(application)
        repository = QuranRepository(db.quranDao())
        audioManager = QuranAudioManager(application)
        audioPlaybackState = audioManager.playbackState

        bookmarks = repository.bookmarks.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        lastRead = repository.lastRead.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        favoriteDuas = repository.favoriteDuas.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    // --- Quran actions ---
    fun onQuranSearchChanged(query: String) {
        _quranState.value = _quranState.value.copy(searchQuery = query)
    }

    fun openSurah(surah: SurahInfo) {
        _quranState.value = _quranState.value.copy(
            activeSurah = surah,
            isLoadingVerses = true,
            activeVerses = emptyList()
        )

        viewModelScope.launch {
            val verses = repository.getSurahVerses(surah.id)
            _quranState.value = _quranState.value.copy(
                activeVerses = verses,
                isLoadingVerses = false
            )
            repository.setLastRead(surah.id, surah.nameArabic, 1)
        }
    }

    fun closeSurahReader() {
        audioManager.stopAll()
        _quranState.value = _quranState.value.copy(
            activeSurah = null,
            activeVerses = emptyList()
        )
    }

    fun adjustFontSize(newSize: Float) {
        _quranState.value = _quranState.value.copy(
            fontSizeSp = newSize.coerceIn(18f, 38f)
        )
    }

    fun toggleTranslation() {
        _quranState.value = _quranState.value.copy(
            showTranslation = !_quranState.value.showTranslation
        )
    }

    fun toggleTafsir() {
        _quranState.value = _quranState.value.copy(
            showTafsir = !_quranState.value.showTafsir
        )
    }

    fun selectReciter(reciter: Reciter) {
        _quranState.value = _quranState.value.copy(selectedReciter = reciter)
    }

    fun playAyahAudio(surahId: Int, ayahNumber: Int) {
        val surahName = _quranState.value.activeSurah?.nameArabic ?: ""
        val reciter = _quranState.value.selectedReciter
        audioManager.playQuranAudio(
            surahId = surahId,
            ayahNumber = ayahNumber,
            surahName = surahName,
            reciterFolder = reciter.baseUrl,
            onAyahCompleted = {
                // Autoplay next verse if available
                val nextAyah = ayahNumber + 1
                val total = _quranState.value.activeSurah?.versesCount ?: 0
                if (nextAyah <= total) {
                    playAyahAudio(surahId, nextAyah)
                }
            }
        )
    }

    fun toggleBookmark(surahId: Int, surahName: String, ayahNumber: Int, text: String, isBookmarked: Boolean) {
        viewModelScope.launch {
            if (isBookmarked) {
                repository.removeBookmarkBySurahAyah(surahId, ayahNumber)
            } else {
                repository.addBookmark(surahId, surahName, ayahNumber, text)
            }
        }
    }

    fun removeBookmark(id: Int) {
        viewModelScope.launch {
            repository.removeBookmark(id)
        }
    }

    // --- Adhkar & Duas actions ---
    fun selectAdhkarCategory(categoryId: String) {
        _adhkarState.value = _adhkarState.value.copy(selectedCategoryId = categoryId)
    }

    fun setAdhkarFilter(filter: AdhkarFilter) {
        _adhkarState.value = _adhkarState.value.copy(filterType = filter)
    }

    fun onAdhkarSearchChanged(query: String) {
        _adhkarState.value = _adhkarState.value.copy(searchQuery = query)
    }

    fun incrementDhikrCount(dhikrId: String, targetCount: Int) {
        val currentCounts = _adhkarState.value.dhikrCounts.toMutableMap()
        val current = currentCounts[dhikrId] ?: 0
        if (current < targetCount) {
            currentCounts[dhikrId] = current + 1
            _adhkarState.value = _adhkarState.value.copy(dhikrCounts = currentCounts)
            triggerVibration(false)
            if (current + 1 == targetCount) {
                triggerVibration(true) // Stronger pulse for completion
            }
        }
    }

    fun resetDhikrCount(dhikrId: String) {
        val currentCounts = _adhkarState.value.dhikrCounts.toMutableMap()
        currentCounts[dhikrId] = 0
        _adhkarState.value = _adhkarState.value.copy(dhikrCounts = currentCounts)
    }

    fun resetAllCountsForCategory(categoryId: String) {
        val items = repository.getAdhkarByCategory(categoryId)
        val currentCounts = _adhkarState.value.dhikrCounts.toMutableMap()
        for (item in items) {
            currentCounts[item.id] = 0
        }
        _adhkarState.value = _adhkarState.value.copy(dhikrCounts = currentCounts)
    }

    fun reciteDhikr(dhikr: DhikrItem) {
        audioManager.reciteDhikr(
            dhikrId = dhikr.id,
            title = dhikr.title,
            text = dhikr.arabicText
        )
    }

    fun stopReciting() {
        audioManager.stopAll()
    }

    fun toggleFavoriteDua(dua: DhikrItem, isCurrentlyFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavoriteDua(dua.id, dua.title, dua.categoryId, isCurrentlyFav)
        }
    }

    // --- Tasbih actions ---
    fun tapTasbih() {
        val state = _tasbihState.value
        val newCount = state.currentCount + 1
        val newTotal = state.totalCount + 1

        if (state.targetCount > 0 && newCount >= state.targetCount) {
            _tasbihState.value = state.copy(
                currentCount = 0,
                lapCount = state.lapCount + 1,
                totalCount = newTotal
            )
            if (state.isVibrationEnabled) triggerVibration(true)
        } else {
            _tasbihState.value = state.copy(
                currentCount = newCount,
                totalCount = newTotal
            )
            if (state.isVibrationEnabled) triggerVibration(false)
        }
    }

    fun resetTasbih() {
        _tasbihState.value = _tasbihState.value.copy(
            currentCount = 0,
            lapCount = 0
        )
    }

    fun selectTasbihPhrase(phrase: String) {
        _tasbihState.value = _tasbihState.value.copy(
            selectedDhikr = phrase,
            currentCount = 0,
            lapCount = 0
        )
    }

    fun setTasbihTarget(target: Int) {
        _tasbihState.value = _tasbihState.value.copy(
            targetCount = target,
            currentCount = 0,
            lapCount = 0
        )
    }

    fun toggleTasbihVibration() {
        _tasbihState.value = _tasbihState.value.copy(
            isVibrationEnabled = !_tasbihState.value.isVibrationEnabled
        )
    }

    private fun triggerVibration(isCompletion: Boolean) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = getApplication<Application>().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                getApplication<Application>().getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            vibrator?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val duration = if (isCompletion) 120L else 35L
                    val amplitude = if (isCompletion) VibrationEffect.DEFAULT_AMPLITUDE else 120
                    it.vibrate(VibrationEffect.createOneShot(duration, amplitude))
                } else {
                    @Suppress("DEPRECATION")
                    it.vibrate(if (isCompletion) 120L else 35L)
                }
            }
        } catch (_: Exception) {}
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.release()
    }
}
