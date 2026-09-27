package com.example.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.example.data.model.CityLocation
import com.example.data.model.PrayerTimings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PrayerRepository {

    // Default primary focus: Algeria Cities (الجزائر الحبيبة وولاياتها الرئيسية)
    val algerianCities = listOf(
        CityLocation("الجزائر العاصمة", "Algiers", "Algeria"),
        CityLocation("وهران", "Oran", "Algeria"),
        CityLocation("قسنطينة", "Constantine", "Algeria"),
        CityLocation("عنابة", "Annaba", "Algeria"),
        CityLocation("سطيف", "Setif", "Algeria"),
        CityLocation("باتنة", "Batna", "Algeria"),
        CityLocation("تلمسان", "Tlemcen", "Algeria"),
        CityLocation("بسكرة", "Biskra", "Algeria"),
        CityLocation("البليدة", "Blida", "Algeria"),
        CityLocation("بجاية", "Bejaia", "Algeria"),
        CityLocation("تيزي وزو", "Tizi Ouzou", "Algeria"),
        CityLocation("ورقلة", "Ouargla", "Algeria"),
        CityLocation("غرداية", "Ghardaia", "Algeria"),
        CityLocation("الشلف", "Chlef", "Algeria"),
        CityLocation("مستغانم", "Mostaganem", "Algeria"),
        CityLocation("سيدي بلعباس", "Sidi Bel Abbes", "Algeria"),
        CityLocation("المسيلة", "MSila", "Algeria"),
        CityLocation("الجلفة", "Djelfa", "Algeria"),
        CityLocation("تبسة", "Tebessa", "Algeria"),
        CityLocation("أدرار", "Adrar", "Algeria"),
        CityLocation("تمنراست", "Tamanrasset", "Algeria")
    )

    // Other Islamic Holy Cities
    val holyCities = listOf(
        CityLocation("مكة المكرمة", "Makkah", "Saudi Arabia"),
        CityLocation("المدينة المنورة", "Madinah", "Saudi Arabia"),
        CityLocation("القدس الشريف", "Jerusalem", "Palestine")
    )

    val allCities = algerianCities + holyCities

    suspend fun getPrayerTimings(city: CityLocation): PrayerTimings = withContext(Dispatchers.IO) {
        try {
            val dateStr = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date())
            // Method 4: Umm al-Qura, Method 3: Muslim World League (often standard for Maghreb/Algeria)
            val method = if (city.country == "Algeria") "3" else "4"
            val urlString = "https://api.aladhan.com/v1/timingsByCity/$dateStr?city=${city.englishName}&country=${city.country}&method=$method"
            val url = URL(urlString)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 6000
                readTimeout = 6000
                requestMethod = "GET"
            }

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                return@withContext parseTimingsResponse(response, dateStr)
            }
        } catch (_: Exception) {
            // Fallback
        }

        fallbackTimings()
    }

    suspend fun getPrayerTimingsByCoordinates(latitude: Double, longitude: Double): Pair<PrayerTimings, String> = withContext(Dispatchers.IO) {
        try {
            val dateStr = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date())
            val urlString = "https://api.aladhan.com/v1/timings/$dateStr?latitude=$latitude&longitude=$longitude&method=3"
            val url = URL(urlString)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 6000
                readTimeout = 6000
                requestMethod = "GET"
            }

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val timings = parseTimingsResponse(response, dateStr)
                return@withContext Pair(timings, "موقعك الحالي بدقة GPS")
            }
        } catch (_: Exception) {
        }
        Pair(fallbackTimings(), "موقعك التقديري")
    }

    @SuppressLint("MissingPermission")
    suspend fun detectCurrentLocation(context: Context): Location? = withContext(Dispatchers.IO) {
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return@withContext null
            val providers = locationManager.getProviders(true)
            var bestLocation: Location? = null
            for (provider in providers) {
                val l = locationManager.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                    bestLocation = l
                }
            }
            bestLocation
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getCityNameFromCoordinates(context: Context, lat: Double, lon: Double): String = withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale("ar"))
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                val locality = addr.locality ?: addr.subAdminArea ?: addr.adminArea ?: "الجزائر"
                return@withContext locality
            }
        } catch (_: Exception) {
        }
        "موقعك الحالي"
    }

    private fun parseTimingsResponse(response: String, fallbackDate: String): PrayerTimings {
        val root = JSONObject(response)
        val data = root.getJSONObject("data")
        val timings = data.getJSONObject("timings")
        val dateObj = data.getJSONObject("date")
        val hijri = dateObj.getJSONObject("hijri")
        val gregorian = dateObj.getJSONObject("gregorian")

        fun clean(t: String): String = t.split(" ").firstOrNull() ?: t

        val hijriDay = hijri.optString("day", "")
        val hijriMonth = hijri.optJSONObject("month")?.optString("ar")
            ?: hijri.optJSONObject("month")?.optString("en", "") ?: ""
        val hijriYear = hijri.optString("year", "")
        val hijriFormatted = "$hijriDay $hijriMonth $hijriYear هـ"

        return PrayerTimings(
            fajr = clean(timings.getString("Fajr")),
            sunrise = clean(timings.getString("Sunrise")),
            dhuhr = clean(timings.getString("Dhuhr")),
            asr = clean(timings.getString("Asr")),
            maghrib = clean(timings.getString("Maghrib")),
            isha = clean(timings.getString("Isha")),
            hijriDate = hijriFormatted,
            gregorianDate = gregorian.optString("date", fallbackDate)
        )
    }

    private fun fallbackTimings(): PrayerTimings {
        val today = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        return PrayerTimings(
            fajr = "05:12",
            sunrise = "06:36",
            dhuhr = "12:44",
            asr = "16:08",
            maghrib = "18:49",
            isha = "20:06",
            hijriDate = "١٤٤٧ هـ",
            gregorianDate = today
        )
    }
}
