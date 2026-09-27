package com.example.ui.screens.prayer

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CityLocation
import com.example.data.model.PrayerTimings
import com.example.data.repository.PrayerRepository
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerTimesScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCity by remember { mutableStateOf(PrayerRepository.algerianCities.first()) }
    var locationDisplayName by remember { mutableStateOf("الجزائر العاصمة (التوقيت الرسمي)") }
    var isUsingAutoGps by remember { mutableStateOf(false) }

    var prayerTimings by remember { mutableStateOf<PrayerTimings?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isDetectingLocation by remember { mutableStateOf(false) }
    var currentTime by remember { mutableStateOf(Date()) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    fun loadCityTimings(city: CityLocation) {
        isLoading = true
        isUsingAutoGps = false
        locationDisplayName = "${city.arabicName} (الجزائر)"
        coroutineScope.launch {
            val timings = PrayerRepository.getPrayerTimings(city)
            prayerTimings = timings
            isLoading = false
        }
    }

    fun detectGpsAndLoadTimings() {
        isDetectingLocation = true
        isLoading = true
        coroutineScope.launch {
            val loc = PrayerRepository.detectCurrentLocation(context)
            if (loc != null) {
                val (timings, desc) = PrayerRepository.getPrayerTimingsByCoordinates(loc.latitude, loc.longitude)
                val detectedName = PrayerRepository.getCityNameFromCoordinates(context, loc.latitude, loc.longitude)
                prayerTimings = timings
                isUsingAutoGps = true
                locationDisplayName = "$detectedName - $desc"
            } else {
                // Default to Algiers if GPS is unavailable
                val timings = PrayerRepository.getPrayerTimings(PrayerRepository.algerianCities.first())
                prayerTimings = timings
                locationDisplayName = "الجزائر العاصمة (الافتراضي)"
            }
            isDetectingLocation = false
            isLoading = false
        }
    }

    // Permission launcher for Location
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            detectGpsAndLoadTimings()
        } else {
            // Default to selected Algeria city
            loadCityTimings(selectedCity)
        }
    }

    // Initial load: Attempt GPS or load first Algeria city
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // Live clock ticker
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = Date()
        }
    }

    val timeFormat = remember { SimpleDateFormat("hh:mm:ss a", Locale.getDefault()) }
    val formattedCurrentTime = remember(currentTime) { timeFormat.format(currentTime) }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("prayer_times_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "مواقيت الصلاة في الجزائر",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = locationDisplayName,
                            fontSize = 11.sp,
                            color = GoldLight,
                            maxLines = 1
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EmeraldDark,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(
                        onClick = {
                            if (isUsingAutoGps) detectGpsAndLoadTimings() else loadCityTimings(selectedCity)
                        },
                        modifier = Modifier.testTag("refresh_prayer_times_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "تحديث المواقيت"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (isLoading && prayerTimings == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = EmeraldPrimary)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = if (isDetectingLocation) "جارٍ تحديد موقعك الجغرافي بالجزائر بدقة..." else "جارٍ جلب مواقيت الصلاة...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            val timings = prayerTimings ?: return@Scaffold
            val nextPrayerInfo = remember(timings, currentTime) {
                calculateNextPrayer(timings, currentTime)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // GPS Auto Detection Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUsingAutoGps) EmeraldContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                        ),
                        border = if (isUsingAutoGps) androidx.compose.foundation.BorderStroke(1.5.dp, EmeraldPrimary) else null,
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = null,
                                tint = if (isUsingAutoGps) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isUsingAutoGps) "مفعل: الموقع التلقائي (GPS)" else "تحديد موقعي الدقيق تلقائياً",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isUsingAutoGps) EmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = locationDisplayName,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Button(
                                onClick = {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isUsingAutoGps) EmeraldPrimary else EmeraldContainer
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (isUsingAutoGps) "محدّث" else "تحديد",
                                    color = if (isUsingAutoGps) Color.White else EmeraldDark,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Algeria Cities Selector Dropdown
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = EmeraldPrimary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "ولاية / مدينة:",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.weight(1f))

                            ExposedDropdownMenuBox(
                                expanded = isDropdownExpanded,
                                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedCity.arabicName,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                                    modifier = Modifier
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                        .width(180.dp),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                ExposedDropdownMenu(
                                    expanded = isDropdownExpanded,
                                    onDismissRequest = { isDropdownExpanded = false }
                                ) {
                                    PrayerRepository.allCities.forEach { city ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(city.arabicName)
                                                    if (city.country == "Algeria") {
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text("🇩🇿", fontSize = 12.sp)
                                                    }
                                                }
                                            },
                                            onClick = {
                                                selectedCity = city
                                                isDropdownExpanded = false
                                                loadCityTimings(city)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Next Prayer Countdown Hero Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(EmeraldDark, EmeraldPrimary)
                                    )
                                )
                                .padding(20.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = timings.hijriDate,
                                        color = GoldLight,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = formattedCurrentTime,
                                        color = Color.White.copy(alpha = 0.8f),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .size(62.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = nextPrayerInfo.icon,
                                        contentDescription = null,
                                        tint = GoldAccent,
                                        modifier = Modifier.size(34.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "الصلاة القادمة: ${nextPrayerInfo.name}",
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = nextPrayerInfo.time,
                                    color = GoldLight,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = Color.Black.copy(alpha = 0.25f)
                                ) {
                                    Text(
                                        text = "متبقي: ${nextPrayerInfo.remaining}",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "مواقيت الصلوات الخمس اليوم في الجزائر",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Prayer List Items
                item {
                    PrayerRow(
                        name = "صلاة الفجر",
                        time = timings.fajr,
                        icon = Icons.Default.WbTwilight,
                        isNext = nextPrayerInfo.name == "الفجر"
                    )
                }
                item {
                    PrayerRow(
                        name = "الشروق",
                        time = timings.sunrise,
                        icon = Icons.Default.WbSunny,
                        isNext = nextPrayerInfo.name == "الشروق",
                        isSecondary = true
                    )
                }
                item {
                    PrayerRow(
                        name = "صلاة الظهر",
                        time = timings.dhuhr,
                        icon = Icons.Default.WbSunny,
                        isNext = nextPrayerInfo.name == "الظهر"
                    )
                }
                item {
                    PrayerRow(
                        name = "صلاة العصر",
                        time = timings.asr,
                        icon = Icons.Default.WbSunny,
                        isNext = nextPrayerInfo.name == "العصر"
                    )
                }
                item {
                    PrayerRow(
                        name = "صلاة المغرب",
                        time = timings.maghrib,
                        icon = Icons.Default.NightsStay,
                        isNext = nextPrayerInfo.name == "المغرب"
                    )
                }
                item {
                    PrayerRow(
                        name = "صلاة العشاء",
                        time = timings.isha,
                        icon = Icons.Default.Bedtime,
                        isNext = nextPrayerInfo.name == "العشاء"
                    )
                }

                // Hadith on prayer
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = EmeraldContainer.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FormatQuote,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "فضل الصلاة على وقتها",
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "سُئل رسول الله ﷺ: أي العمل أحب إلى الله؟ قال: «الصلاة على وقتها». (متفق عليه)",
                                style = MaterialTheme.typography.bodySmall,
                                lineHeight = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
private fun PrayerRow(
    name: String,
    time: String,
    icon: ImageVector,
    isNext: Boolean,
    isSecondary: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("prayer_row_${name}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isNext) EmeraldPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        border = if (isNext) androidx.compose.foundation.BorderStroke(1.5.dp, EmeraldPrimary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        if (isNext) EmeraldPrimary
                        else if (isSecondary) GoldLight.copy(alpha = 0.5f)
                        else EmeraldContainer
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isNext) Color.White else EmeraldPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = name,
                fontWeight = if (isNext) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 15.sp,
                color = if (isNext) EmeraldPrimary else MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isNext) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EmeraldPrimary
                ) {
                    Text(
                        text = "التالية",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
            }

            Text(
                text = time,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = if (isNext) EmeraldPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private data class NextPrayerInfo(
    val name: String,
    val time: String,
    val remaining: String,
    val icon: ImageVector
)

private fun calculateNextPrayer(timings: PrayerTimings, now: Date): NextPrayerInfo {
    val list = listOf(
        Triple("الفجر", timings.fajr, Icons.Default.WbTwilight),
        Triple("الشروق", timings.sunrise, Icons.Default.WbSunny),
        Triple("الظهر", timings.dhuhr, Icons.Default.WbSunny),
        Triple("العصر", timings.asr, Icons.Default.WbSunny),
        Triple("المغرب", timings.maghrib, Icons.Default.NightsStay),
        Triple("العشاء", timings.isha, Icons.Default.Bedtime)
    )

    val cal = java.util.Calendar.getInstance()
    cal.time = now

    for ((pName, pTime, pIcon) in list) {
        val parts = pTime.split(":")
        if (parts.size >= 2) {
            val h = parts[0].toIntOrNull() ?: 0
            val m = parts[1].toIntOrNull() ?: 0
            val pCal = java.util.Calendar.getInstance().apply {
                time = now
                set(java.util.Calendar.HOUR_OF_DAY, h)
                set(java.util.Calendar.MINUTE, m)
                set(java.util.Calendar.SECOND, 0)
            }

            if (pCal.after(cal)) {
                val diffMillis = pCal.timeInMillis - cal.timeInMillis
                val hours = diffMillis / (1000 * 60 * 60)
                val mins = (diffMillis / (1000 * 60)) % 60
                val secs = (diffMillis / 1000) % 60
                val remainingStr = if (hours > 0) "$hours س و $mins د و $secs ث" else "$mins د و $secs ث"
                return NextPrayerInfo(pName, pTime, remainingStr, pIcon)
            }
        }
    }

    return NextPrayerInfo("الفجر (غداً)", timings.fajr, "الفجر القادم", Icons.Default.WbTwilight)
}
