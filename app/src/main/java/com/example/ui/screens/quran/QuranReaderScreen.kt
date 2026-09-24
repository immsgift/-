package com.example.ui.screens.quran

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioPlaybackState
import com.example.data.local.BookmarkEntity
import com.example.data.model.AvailableReciters
import com.example.data.model.Ayah
import com.example.data.model.Reciter
import com.example.data.model.SurahInfo
import com.example.ui.theme.AyahSeparatorGold
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranReaderScreen(
    surah: SurahInfo,
    verses: List<Ayah>,
    isLoading: Boolean,
    fontSizeSp: Float,
    showTranslation: Boolean,
    showTafsir: Boolean,
    selectedReciter: Reciter,
    playbackState: AudioPlaybackState,
    bookmarks: List<BookmarkEntity>,
    onBack: () -> Unit,
    onAdjustFontSize: (Float) -> Unit,
    onToggleTranslation: () -> Unit,
    onToggleTafsir: () -> Unit,
    onSelectReciter: (Reciter) -> Unit,
    onPlayAyah: (Int) -> Unit,
    onToggleBookmark: (Ayah, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showFontDialog by remember { mutableStateOf(false) }
    var showReciterMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("quran_reader_screen")
    ) {
        // Reader Top App Bar
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "سُورَةُ ${surah.nameArabic}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${surah.nameEnglish} • ${surah.versesCount} آيات • ${surah.revelationType.arabicLabel}",
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldLight,
                        fontSize = 11.sp
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "رجوع",
                        tint = Color.White
                    )
                }
            },
            actions = {
                // Font Size Button
                IconButton(onClick = { showFontDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.FormatSize,
                        contentDescription = "حجم الخط",
                        tint = Color.White
                    )
                }
                // Translation Toggle
                IconButton(onClick = onToggleTranslation) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = "الترجمة",
                        tint = if (showTranslation) GoldAccent else Color.White.copy(alpha = 0.6f)
                    )
                }
                // Tafsir Toggle
                IconButton(onClick = onToggleTafsir) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "التفسير",
                        tint = if (showTafsir) GoldAccent else Color.White.copy(alpha = 0.6f)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = EmeraldDark
            )
        )

        // Audio Reciter Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Reciter dropdown button
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showReciterMenu = true }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "القارئ: ${selectedReciter.nameArabic}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    DropdownMenu(
                        expanded = showReciterMenu,
                        onDismissRequest = { showReciterMenu = false }
                    ) {
                        AvailableReciters.forEach { r ->
                            DropdownMenuItem(
                                text = { Text(r.nameArabic) },
                                onClick = {
                                    onSelectReciter(r)
                                    showReciterMenu = false
                                }
                            )
                        }
                    }
                }

                // Play first ayah button
                val isPlayingThisSurah = playbackState.isPlaying && playbackState.activeSurahId == surah.id
                Button(
                    onClick = {
                        if (isPlayingThisSurah) {
                            // pause handled in miniplayer
                        } else {
                            onPlayAyah(1)
                        }
                    },
                    modifier = Modifier.height(34.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Icon(
                        imageVector = if (isPlayingThisSurah) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isPlayingThisSurah) "جارٍ التشغيل" else "استماع للسورة",
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Verses List or Loading
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = EmeraldPrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("جارٍ تحميل الآيات الكريمة...")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
            ) {
                // Bismillah Header (Except Surah 9 At-Tawbah)
                if (surah.id != 9) {
                    item {
                        BismillahHeader()
                    }
                }

                // Verses
                items(verses, key = { it.verseNumber }) { ayah ->
                    val isAyahPlaying = playbackState.isPlaying &&
                            playbackState.activeSurahId == surah.id &&
                            playbackState.activeAyahNumber == ayah.verseNumber

                    val isBookmarked = bookmarks.any {
                        it.surahId == surah.id && it.ayahNumber == ayah.verseNumber
                    }

                    AyahCard(
                        ayah = ayah,
                        surahName = surah.nameArabic,
                        fontSizeSp = fontSizeSp,
                        showTranslation = showTranslation,
                        showTafsir = showTafsir,
                        isPlaying = isAyahPlaying,
                        isBookmarked = isBookmarked,
                        onPlay = { onPlayAyah(ayah.verseNumber) },
                        onBookmark = { onToggleBookmark(ayah, isBookmarked) },
                        onCopy = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Ayah", "${ayah.textArabic} [سورة ${surah.nameArabic}: ${ayah.verseNumber}]"))
                            Toast.makeText(context, "تم نسخ الآية الكريمة", Toast.LENGTH_SHORT).show()
                        },
                        onShare = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "﴿ ${ayah.textArabic} ﴾\n[سورة ${surah.nameArabic}: ${ayah.verseNumber}]\n\nتمت المشاركة من تطبيق القرآن الكريم")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "مشاركة الآية"))
                        }
                    )
                }
            }
        }
    }

    // Font size adjustment dialog
    if (showFontDialog) {
        AlertDialog(
            onDismissRequest = { showFontDialog = false },
            title = { Text("تعديل حجم الخط") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        fontSize = fontSizeSp.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    Slider(
                        value = fontSizeSp,
                        onValueChange = onAdjustFontSize,
                        valueRange = 18f..38f,
                        steps = 8
                    )
                    Text(
                        text = "${fontSizeSp.toInt()} sp",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showFontDialog = false }) {
                    Text("تم")
                }
            }
        )
    }
}

@Composable
fun BismillahHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Text(
                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = EmeraldPrimary,
                modifier = Modifier.padding(vertical = 14.dp, horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun AyahCard(
    ayah: Ayah,
    surahName: String,
    fontSizeSp: Float,
    showTranslation: Boolean,
    showTafsir: Boolean,
    isPlaying: Boolean,
    isBookmarked: Boolean,
    onPlay: () -> Unit,
    onBookmark: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .testTag("ayah_card_${ayah.verseNumber}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlaying) EmeraldContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPlaying) 4.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Arabic Text with ornamental Ayah number
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Ayah Number Badge
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(GoldAccent.copy(alpha = 0.2f))
                        .border(1.dp, GoldAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ayah.verseNumber.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Quranic Arabic Text
                Text(
                    text = "${ayah.textArabic} ﴿${ayah.verseNumber}﴾",
                    fontSize = fontSizeSp.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = (fontSizeSp * 1.7f).sp,
                    textAlign = TextAlign.Right,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }

            // Translation (if enabled)
            if (showTranslation && ayah.textTranslation.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = ayah.textTranslation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }

            // Tafsir (if enabled)
            if (showTafsir && ayah.textTafsir.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "التفسير الميسر:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = ayah.textTafsir,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action toolbar for this Ayah
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Play Ayah
                IconButton(onClick = onPlay, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "استماع للآية",
                        tint = if (isPlaying) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Bookmark Ayah
                IconButton(onClick = onBookmark, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "حفظ فاصلة",
                        tint = if (isBookmarked) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Copy Ayah
                IconButton(onClick = onCopy, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "نسخ",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Share Ayah
                IconButton(onClick = onShare, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "مشاركة",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
