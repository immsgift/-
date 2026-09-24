package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.repository.AdhkarDataSources
import com.example.data.repository.QuranDataSources
import com.example.ui.components.AudioMiniPlayer
import com.example.ui.navigation.QuranBottomNavigationBar
import com.example.ui.navigation.QuranNavDestination
import com.example.ui.screens.adhkar.AdhkarScreen
import com.example.ui.screens.bookmarks.BookmarksScreen
import com.example.ui.screens.quran.QuranHomeScreen
import com.example.ui.screens.quran.QuranReaderScreen
import com.example.ui.screens.tasbih.TasbihScreen
import com.example.ui.theme.QuranKarimTheme
import com.example.ui.viewmodel.QuranViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: QuranViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            QuranKarimTheme {
                // Arabic Right-to-Left Layout Direction
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    QuranAppRoot(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun QuranAppRoot(viewModel: QuranViewModel) {
    var currentDestination by remember { mutableStateOf(QuranNavDestination.QURAN) }

    val quranState by viewModel.quranState.collectAsStateWithLifecycle()
    val adhkarState by viewModel.adhkarState.collectAsStateWithLifecycle()
    val tasbihState by viewModel.tasbihState.collectAsStateWithLifecycle()
    val audioState by viewModel.audioPlaybackState.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val lastRead by viewModel.lastRead.collectAsStateWithLifecycle()
    val favoriteDuas by viewModel.favoriteDuas.collectAsStateWithLifecycle()

    // Handle back button when reader is open
    if (quranState.activeSurah != null) {
        BackHandler {
            viewModel.closeSurahReader()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Only show bottom navigation when not in the full immersion reader mode
            if (quranState.activeSurah == null) {
                QuranBottomNavigationBar(
                    currentDestination = currentDestination,
                    onNavigateTo = { dest ->
                        currentDestination = dest
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Content Screens
            when (currentDestination) {
                QuranNavDestination.QURAN -> {
                    if (quranState.activeSurah != null) {
                        QuranReaderScreen(
                            surah = quranState.activeSurah!!,
                            verses = quranState.activeVerses,
                            isLoading = quranState.isLoadingVerses,
                            fontSizeSp = quranState.fontSizeSp,
                            showTranslation = quranState.showTranslation,
                            showTafsir = quranState.showTafsir,
                            selectedReciter = quranState.selectedReciter,
                            playbackState = audioState,
                            bookmarks = bookmarks,
                            onBack = { viewModel.closeSurahReader() },
                            onAdjustFontSize = { viewModel.adjustFontSize(it) },
                            onToggleTranslation = { viewModel.toggleTranslation() },
                            onToggleTafsir = { viewModel.toggleTafsir() },
                            onSelectReciter = { viewModel.selectReciter(it) },
                            onPlayAyah = { ayahNum ->
                                viewModel.playAyahAudio(quranState.activeSurah!!.id, ayahNum)
                            },
                            onToggleBookmark = { ayah, isBookmarked ->
                                viewModel.toggleBookmark(
                                    surahId = quranState.activeSurah!!.id,
                                    surahName = quranState.activeSurah!!.nameArabic,
                                    ayahNumber = ayah.verseNumber,
                                    text = ayah.textArabic,
                                    isBookmarked = isBookmarked
                                )
                            }
                        )
                    } else {
                        QuranHomeScreen(
                            allSurahs = QuranDataSources.allSurahs,
                            searchQuery = quranState.searchQuery,
                            lastRead = lastRead,
                            onSearchChanged = { viewModel.onQuranSearchChanged(it) },
                            onSurahSelected = { surah ->
                                viewModel.openSurah(surah)
                            }
                        )
                    }
                }

                QuranNavDestination.ADHKAR -> {
                    AdhkarScreen(
                        categories = AdhkarDataSources.categories,
                        selectedCategoryId = adhkarState.selectedCategoryId,
                        filterType = adhkarState.filterType,
                        searchQuery = adhkarState.searchQuery,
                        dhikrCounts = adhkarState.dhikrCounts,
                        favoriteDuas = favoriteDuas,
                        playbackState = audioState,
                        onCategorySelected = { viewModel.selectAdhkarCategory(it) },
                        onFilterChanged = { viewModel.setAdhkarFilter(it) },
                        onSearchChanged = { viewModel.onAdhkarSearchChanged(it) },
                        onIncrementCount = { id, target -> viewModel.incrementDhikrCount(id, target) },
                        onResetCount = { id -> viewModel.resetDhikrCount(id) },
                        onResetCategory = { catId -> viewModel.resetAllCountsForCategory(catId) },
                        onReciteDhikr = { item -> viewModel.reciteDhikr(item) },
                        onStopReciting = { viewModel.stopReciting() },
                        onToggleFavorite = { item, isFav -> viewModel.toggleFavoriteDua(item, isFav) }
                    )
                }

                QuranNavDestination.TASBIH -> {
                    TasbihScreen(
                        state = tasbihState,
                        onTap = { viewModel.tapTasbih() },
                        onReset = { viewModel.resetTasbih() },
                        onSelectPhrase = { viewModel.selectTasbihPhrase(it) },
                        onSetTarget = { viewModel.setTasbihTarget(it) },
                        onToggleVibration = { viewModel.toggleTasbihVibration() }
                    )
                }

                QuranNavDestination.BOOKMARKS -> {
                    BookmarksScreen(
                        bookmarks = bookmarks,
                        favoriteDuas = favoriteDuas,
                        allSurahs = QuranDataSources.allSurahs,
                        onSurahSelected = { surah ->
                            currentDestination = QuranNavDestination.QURAN
                            viewModel.openSurah(surah)
                        },
                        onDeleteBookmark = { id -> viewModel.removeBookmark(id) },
                        onReciteDua = { dua -> viewModel.reciteDhikr(dua) },
                        onOpenKhatmDua = {
                            currentDestination = QuranNavDestination.ADHKAR
                            viewModel.selectAdhkarCategory("khatm")
                        }
                    )
                }
            }

            // Floating Mini Audio Player when audio or recitation is active
            AudioMiniPlayer(
                playbackState = audioState,
                onPlayPause = {
                    if (audioState.isPlaying) {
                        viewModel.audioManager.pause()
                    } else {
                        viewModel.audioManager.resume()
                    }
                },
                onStop = {
                    viewModel.audioManager.stopAll()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = if (quranState.activeSurah == null) 8.dp else 16.dp)
            )
        }
    }
}
