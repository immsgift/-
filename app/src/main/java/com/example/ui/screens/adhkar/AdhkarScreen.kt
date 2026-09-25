package com.example.ui.screens.adhkar

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioPlaybackState
import com.example.data.local.FavoriteDuaEntity
import com.example.data.model.DhikrCategory
import com.example.data.model.DhikrItem
import com.example.data.repository.AdhkarDataSources
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldLight
import com.example.ui.viewmodel.AdhkarFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdhkarScreen(
    categories: List<DhikrCategory>,
    selectedCategoryId: String,
    filterType: AdhkarFilter,
    searchQuery: String,
    dhikrCounts: Map<String, Int>,
    favoriteDuas: List<FavoriteDuaEntity>,
    playbackState: AudioPlaybackState,
    onCategorySelected: (String) -> Unit,
    onFilterChanged: (AdhkarFilter) -> Unit,
    onSearchChanged: (String) -> Unit,
    onIncrementCount: (String, Int) -> Unit,
    onResetCount: (String) -> Unit,
    onResetCategory: (String) -> Unit,
    onReciteDhikr: (DhikrItem) -> Unit,
    onStopReciting: () -> Unit,
    onToggleFavorite: (DhikrItem, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Filter categories based on Daily vs Occasion
    val displayedCategories = remember(filterType, categories) {
        when (filterType) {
            AdhkarFilter.ALL -> categories
            AdhkarFilter.DAILY -> categories.filter { !it.isOccasion }
            AdhkarFilter.OCCASIONS -> categories.filter { it.isOccasion }
        }
    }

    // Filter items based on active category and search
    val displayedItems = remember(selectedCategoryId, searchQuery, filterType) {
        if (searchQuery.isNotBlank()) {
            val q = searchQuery.trim().lowercase()
            AdhkarDataSources.allAdhkarItems.filter {
                it.title.lowercase().contains(q) ||
                it.arabicText.lowercase().contains(q) ||
                it.translation.lowercase().contains(q) ||
                it.virtue.lowercase().contains(q)
            }
        } else {
            AdhkarDataSources.allAdhkarItems.filter { it.categoryId == selectedCategoryId }
        }
    }

    val currentCategory = categories.firstOrNull { it.id == selectedCategoryId }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("adhkar_screen_list"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Hero Header
        item {
            AdhkarHeroHeader(
                activeCategory = currentCategory,
                itemsCount = displayedItems.size
            )
        }

        // Search Bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("adhkar_search_input"),
                    placeholder = { Text("ابحث في الأدعية والأذكار (مثل: السفر، الهم، الاستغفار)...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "بحث",
                            tint = EmeraldPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChanged("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "مسح")
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    singleLine = true
                )
            }
        }

        // Section Filters (All, Daily Adhkar, Occasion Duas)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdhkarFilter.entries.forEach { filter ->
                    val isSelected = filter == filterType
                    FilterChip(
                        selected = isSelected,
                        onClick = { onFilterChanged(filter) },
                        label = { Text(filter.label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EmeraldPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Horizontal Category Chips
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(displayedCategories, key = { it.id }) { cat ->
                    val isSelected = cat.id == selectedCategoryId && searchQuery.isEmpty()
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                onSearchChanged("")
                                onCategorySelected(cat.id)
                            }
                            .testTag("category_chip_${cat.id}"),
                        color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(20.dp),
                        tonalElevation = if (isSelected) 4.dp else 1.dp
                    ) {
                        Text(
                            text = cat.titleArabic,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Category Controls Bar (Listen hands-free / Reset category counters)
        if (searchQuery.isEmpty() && displayedItems.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isRecitingAny = playbackState.isPlaying && playbackState.activeDhikrId != null

                    // Listen Hands-Free Button
                    OutlinedButton(
                        onClick = {
                            if (isRecitingAny) {
                                onStopReciting()
                            } else {
                                val first = displayedItems.firstOrNull()
                                if (first != null) onReciteDhikr(first)
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = if (isRecitingAny) Icons.Default.Stop else Icons.Default.Headphones,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isRecitingAny) "إيقاف الاستماع" else "استماع متتالي للأذكار",
                            color = EmeraldPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Reset Counters Button
                    IconButton(
                        onClick = { onResetCategory(selectedCategoryId) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "تصفير العدادات",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // List of Dhikr and Dua Cards
        items(displayedItems, key = { it.id }) { item ->
            val count = dhikrCounts[item.id] ?: 0
            val isFavorite = favoriteDuas.any { it.duaId == item.id }
            val isPlaying = playbackState.isPlaying && playbackState.activeDhikrId == item.id

            DhikrItemCard(
                item = item,
                currentCount = count,
                isFavorite = isFavorite,
                isPlayingAudio = isPlaying,
                onIncrement = { onIncrementCount(item.id, item.targetCount) },
                onReset = { onResetCount(item.id) },
                onRecite = {
                    if (isPlaying) onStopReciting() else onReciteDhikr(item)
                },
                onToggleFavorite = { onToggleFavorite(item, isFavorite) },
                onCopy = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Dhikr", "${item.arabicText}\n[${item.title} - ${item.reference}]"))
                    Toast.makeText(context, "تم نسخ الذكر", Toast.LENGTH_SHORT).show()
                },
                onShare = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "${item.title}\n\n${item.arabicText}\n\n${item.reference}\n${item.virtue}\n\nتمت المشاركة من تطبيق IMANI | إِمَانِي")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "مشاركة الذكر"))
                },
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun AdhkarHeroHeader(
    activeCategory: DhikrCategory?,
    itemsCount: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        EmeraldDark,
                        EmeraldPrimary
                    )
                )
            )
            .padding(top = 18.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "الأذكار والأدعية",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldLight,
                        fontSize = 13.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(GoldAccent.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Active category indicator pill
            if (activeCategory != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${activeCategory.titleArabic} • ${activeCategory.countHint}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "$itemsCount أذكار/أدعية",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldLight,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DhikrItemCard(
    item: DhikrItem,
    currentCount: Int,
    isFavorite: Boolean,
    isPlayingAudio: Boolean,
    onIncrement: () -> Unit,
    onReset: () -> Unit,
    onRecite: () -> Unit,
    onToggleFavorite: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCompleted = currentCount >= item.targetCount

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dhikr_card_${item.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) Color(0xFFF1F8F4) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Title & Action buttons (Audio, Favorite, Share)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Title and Reference
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (item.reference.isNotEmpty()) {
                        Text(
                            text = item.reference,
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldPrimary,
                            fontSize = 11.sp
                        )
                    }
                }

                // Quick Listen Button
                IconButton(
                    onClick = onRecite,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isPlayingAudio) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant)
                        .testTag("recite_audio_btn_${item.id}")
                ) {
                    Icon(
                        imageVector = if (isPlayingAudio) Icons.Default.Stop else Icons.Default.VolumeUp,
                        contentDescription = "استماع للذكر",
                        tint = if (isPlayingAudio) Color.White else EmeraldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Favorite Button
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "المفضلة",
                        tint = if (isFavorite) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Arabic Dhikr / Dua Text
            Text(
                text = item.arabicText,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 32.sp,
                textAlign = TextAlign.Right,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            // Virtue / Benefit Box (الفضل)
            if (item.virtue.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = GoldContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "الفضل: ",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7E5C00)
                        )
                        Text(
                            text = item.virtue,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Translation (if available)
            if (item.translation.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.translation,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            Spacer(modifier = Modifier.height(10.dp))

            // Interactive Counter & Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Secondary actions: Copy & Share
                Row {
                    IconButton(onClick = onCopy, modifier = Modifier.size(34.dp)) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "نسخ",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(onClick = onShare, modifier = Modifier.size(34.dp)) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "مشاركة",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    if (currentCount > 0) {
                        IconButton(onClick = onReset, modifier = Modifier.size(34.dp)) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "تصفير",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Interactive Counter Button
                Button(
                    onClick = onIncrement,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCompleted) EmeraldPrimary else EmeraldDark
                    ),
                    modifier = Modifier.testTag("dhikr_counter_btn_${item.id}")
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "تم",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "اكتمل (${item.targetCount})",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "قراءة: $currentCount / ${item.targetCount}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
