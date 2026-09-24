package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent

enum class QuranNavDestination(
    val route: String,
    val titleArabic: String,
    val icon: ImageVector,
    val testTag: String
) {
    QURAN("quran", "القرآن", Icons.AutoMirrored.Filled.MenuBook, "nav_item_quran"),
    ADHKAR("adhkar", "الأذكار والأدعية", Icons.Default.WbSunny, "nav_item_adhkar"),
    TASBIH("tasbih", "السبحة", Icons.Default.DonutLarge, "nav_item_tasbih"),
    BOOKMARKS("bookmarks", "المحفوظات", Icons.Default.Bookmark, "nav_item_bookmarks")
}

@Composable
fun QuranBottomNavigationBar(
    currentDestination: QuranNavDestination,
    onNavigateTo: (QuranNavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("quran_bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = EmeraldPrimary
    ) {
        QuranNavDestination.entries.forEach { destination ->
            val isSelected = destination == currentDestination
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigateTo(destination) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.titleArabic
                    )
                },
                label = {
                    Text(
                        text = destination.titleArabic,
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = EmeraldPrimary,
                    indicatorColor = EmeraldPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(destination.testTag)
            )
        }
    }
}
