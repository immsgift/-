package com.example.ui.screens.tasbih

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldLight
import com.example.ui.viewmodel.PresetTasbihPhrases
import com.example.ui.viewmodel.TasbihUiState

@Composable
fun TasbihScreen(
    state: TasbihUiState,
    onTap: () -> Unit,
    onReset: () -> Unit,
    onSelectPhrase: (String) -> Unit,
    onSetTarget: (Int) -> Unit,
    onToggleVibration: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
            .testTag("tasbih_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hero Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(EmeraldDark, EmeraldPrimary)
                    )
                )
                .padding(top = 18.dp, bottom = 18.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "السبحة الإلكترونية",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "فَسَبِّحْ بِحَمْدِ رَبِّكَ وَكُن مِّنَ السَّاجِدِينَ",
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldLight,
                        fontSize = 13.sp
                    )
                }

                // Vibration Toggle Icon
                IconButton(
                    onClick = onToggleVibration,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (state.isVibrationEnabled) GoldAccent else Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Vibration,
                        contentDescription = "الاهتزاز",
                        tint = if (state.isVibrationEnabled) Color.Black else Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Phrase Selector Chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(PresetTasbihPhrases) { phrase ->
                val isSelected = phrase == state.selectedDhikr
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectPhrase(phrase) },
                    color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = if (isSelected) 4.dp else 1.dp
                ) {
                    Text(
                        text = phrase,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Target Mode Selector (33, 100, Free)
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf(33 to "٣٣ تسبيحة", 100 to "١٠٠ تسبيحة", 0 to "مفتوح (حر)").forEach { (target, label) ->
                val isSelected = state.targetCount == target
                FilterChip(
                    selected = isSelected,
                    onClick = { onSetTarget(target) },
                    label = { Text(label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldAccent,
                        selectedLabelColor = Color.Black
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // Display Active Dhikr Text
        Text(
            text = state.selectedDhikr,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Main Circular Interactive Masbaha Button
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(EmeraldPrimary, EmeraldDark)
                    )
                )
                .border(4.dp, GoldAccent, CircleShape)
                .clickable { onTap() }
                .testTag("tasbih_tap_button"),
            contentAlignment = Alignment.Center
        ) {
            // Circular Progress if target > 0
            if (state.targetCount > 0) {
                val progress = (state.currentCount.toFloat() / state.targetCount.toFloat()).coerceIn(0f, 1f)
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(232.dp),
                    color = GoldAccent,
                    strokeWidth = 6.dp,
                    trackColor = Color.White.copy(alpha = 0.1f)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = state.currentCount.toString(),
                    fontSize = 58.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                if (state.targetCount > 0) {
                    Text(
                        text = "الهدف: ${state.targetCount}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GoldLight,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        text = "تسبيح حر",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GoldLight
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "اضغط للتسبيح",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // Bottom Stats & Reset Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Rounds
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "الدورات المكتملة",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = state.lapCount.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                }

                // Total Count
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "المجموع الكلي",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = state.totalCount.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                }

                // Reset Button
                IconButton(
                    onClick = onReset,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .testTag("tasbih_reset_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "إعادة ضبط",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
