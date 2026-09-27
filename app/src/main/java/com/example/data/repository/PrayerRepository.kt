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

    // جميع ولايات الجزائر الـ 48 ولاية كاملة ومرتبة بالترقيم الرسمي
    val algerianCities = listOf(
        CityLocation("01 - أدرار", "Adrar", "Algeria"),
        CityLocation("02 - الشلف", "Chlef", "Algeria"),
        CityLocation("03 - الأغواط", "Laghouat", "Algeria"),
        CityLocation("04 - أم البواقي", "Oum El Bouaghi", "Algeria"),
        CityLocation("05 - باتنة", "Batna", "Algeria"),
        CityLocation("06 - بجاية", "Bejaia", "Algeria"),
        CityLocation("07 - بسكرة", "Biskra", "Algeria"),
        CityLocation("08 - بشار", "Bechar", "Algeria"),
        CityLocation("09 - البليدة", "Blida", "Algeria"),
        CityLocation("10 - البويرة", "Bouira", "Algeria"),
        CityLocation("11 - تمنراست", "Tamanrasset", "Algeria"),
        CityLocation("12 - تبسة", "Tebessa", "Algeria"),
        CityLocation("13 - تلمسان", "Tlemcen", "Algeria"),
        CityLocation("14 - تيارت", "Tiaret", "Algeria"),
        CityLocation("15 - تيزي وزو", "Tizi Ouzou", "Algeria"),
        CityLocation("16 - الجزائر العاصمة", "Algiers", "Algeria"),
        CityLocation("17 - الجلفة", "Djelfa", "Algeria"),
        CityLocation("18 - جيجل", "Jijel", "Algeria"),
        CityLocation("19 - سطيف", "Setif", "Algeria"),
        CityLocation("20 - سعيدة", "Saida", "Algeria"),
        CityLocation("21 - سكيكدة", "Skikda", "Algeria"),
        CityLocation("22 - سيدي بلعباس", "Sidi Bel Abbes", "Algeria"),
        CityLocation("23 - عنابة", "Annaba", "Algeria"),
        CityLocation("24 - قالمة", "Guelma", "Algeria"),
        CityLocation("25 - قسنطينة", "Constantine", "Algeria"),
        CityLocation("26 - المدية", "Medea", "Algeria"),
        CityLocation("27 - مستغانم", "Mostaganem", "Algeria"),
        CityLocation("28 - المسيلة", "MSila", "Algeria"),
        CityLocation("29 - معسكر", "Mascara", "Algeria"),
        CityLocation("30 - ورقلة", "Ouargla", "Algeria"),
        CityLocation("31 - وهران", "Oran", "Algeria"),
        CityLocation("32 - البيض", "El Bayadh", "Algeria"),
        CityLocation("33 - إليزي", "Illizi", "Algeria"),
        CityLocation("34 - برج بوعريريج", "Bordj Bou Arreridj", "Algeria"),
        CityLocation("35 - بومرداس", "Boumerdes", "Algeria"),
        CityLocation("36 - الطارف", "El Tarf", "Algeria"),
        CityLocation("37 - تندوف", "Tindouf", "Algeria"),
        CityLocation("38 - تسمسيلت", "Tissemsilt", "Algeria"),
        CityLocation("39 - الوادي", "El Oued", "Algeria"),
        CityLocation("40 - خنشلة", "Khenchela", "Algeria"),
        CityLocation("41 - سوق أهراس", "Souk Ahras", "Algeria"),
        CityLocation("42 - تيبازة", "Tipaza", "Algeria"),
        CityLocation("43 - ميلة", "Mila", "Algeria"),
        CityLocation("44 - عين الدفلى", "Ain Defla", "Algeria"),
        CityLocation("45 - النعامة", "Naama", "Algeria"),
        CityLocation("46 - عين تموشنت", "Ain Temouchent", "Algeria"),
        CityLocation("47 - غرداية", "Ghardaia", "Algeria"),
        CityLocation("48 - غليزان", "Relizane", "Algeria")
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
