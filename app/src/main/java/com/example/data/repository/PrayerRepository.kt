package com.example.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.example.data.model.CityLocation
import com.example.data.model.PrayerTimings
import com.example.data.util.PrayerCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PrayerRepository {

    // جميع ولايات الجزائر الـ 48 ولاية كاملة مع إحداثياتها الجغرافية الدقيقة للحساب الفلكي الأوفلاين
    val algerianCities = listOf(
        CityLocation("01 - أدرار", "Adrar", "Algeria", 27.8743, -0.2939),
        CityLocation("02 - الشلف", "Chlef", "Algeria", 36.1652, 1.3345),
        CityLocation("03 - الأغواط", "Laghouat", "Algeria", 33.8000, 2.8651),
        CityLocation("04 - أم البواقي", "Oum El Bouaghi", "Algeria", 35.8755, 7.1135),
        CityLocation("05 - باتنة", "Batna", "Algeria", 35.5560, 6.1741),
        CityLocation("06 - بجاية", "Bejaia", "Algeria", 36.7559, 5.0843),
        CityLocation("07 - بسكرة", "Biskra", "Algeria", 34.8504, 5.7281),
        CityLocation("08 - بشار", "Bechar", "Algeria", 31.6167, -2.2167),
        CityLocation("09 - البليدة", "Blida", "Algeria", 36.4700, 2.8300),
        CityLocation("10 - البويرة", "Bouira", "Algeria", 36.3749, 3.9020),
        CityLocation("11 - تمنراست", "Tamanrasset", "Algeria", 22.7850, 5.5228),
        CityLocation("12 - تبسة", "Tebessa", "Algeria", 35.4042, 8.1242),
        CityLocation("13 - تلمسان", "Tlemcen", "Algeria", 34.8783, -1.3150),
        CityLocation("14 - تيارت", "Tiaret", "Algeria", 35.3710, 1.3170),
        CityLocation("15 - تيزي وزو", "Tizi Ouzou", "Algeria", 36.7118, 4.0459),
        CityLocation("16 - الجزائر العاصمة", "Algiers", "Algeria", 36.7538, 3.0588),
        CityLocation("17 - الجلفة", "Djelfa", "Algeria", 34.6728, 3.2630),
        CityLocation("18 - جيجل", "Jijel", "Algeria", 36.8206, 5.7667),
        CityLocation("19 - سطيف", "Setif", "Algeria", 36.1911, 5.4137),
        CityLocation("20 - سعيدة", "Saida", "Algeria", 34.8303, 0.1517),
        CityLocation("21 - سكيكدة", "Skikda", "Algeria", 36.8792, 6.9075),
        CityLocation("22 - سيدي بلعباس", "Sidi Bel Abbes", "Algeria", 35.1899, -0.6308),
        CityLocation("23 - عنابة", "Annaba", "Algeria", 36.9000, 7.7667),
        CityLocation("24 - قالمة", "Guelma", "Algeria", 36.4622, 7.4261),
        CityLocation("25 - قسنطينة", "Constantine", "Algeria", 36.3650, 6.6147),
        CityLocation("26 - المدية", "Medea", "Algeria", 36.2642, 2.7539),
        CityLocation("27 - مستغانم", "Mostaganem", "Algeria", 35.9311, 0.0892),
        CityLocation("28 - المسيلة", "MSila", "Algeria", 35.7058, 4.5419),
        CityLocation("29 - معسكر", "Mascara", "Algeria", 35.3947, 0.1403),
        CityLocation("30 - ورقلة", "Ouargla", "Algeria", 31.9493, 5.3250),
        CityLocation("31 - وهران", "Oran", "Algeria", 35.6971, -0.6308),
        CityLocation("32 - البيض", "El Bayadh", "Algeria", 33.6832, 1.0193),
        CityLocation("33 - إليزي", "Illizi", "Algeria", 26.4833, 8.4667),
        CityLocation("34 - برج بوعريريج", "Bordj Bou Arreridj", "Algeria", 36.0732, 4.7611),
        CityLocation("35 - بومرداس", "Boumerdes", "Algeria", 36.7664, 3.4772),
        CityLocation("36 - الطارف", "El Tarf", "Algeria", 36.7672, 8.3139),
        CityLocation("37 - تندوف", "Tindouf", "Algeria", 27.6742, -8.1478),
        CityLocation("38 - تسمسيلت", "Tissemsilt", "Algeria", 35.6072, 1.8108),
        CityLocation("39 - الوادي", "El Oued", "Algeria", 33.3683, 6.8675),
        CityLocation("40 - خنشلة", "Khenchela", "Algeria", 35.4358, 7.1433),
        CityLocation("41 - سوق أهراس", "Souk Ahras", "Algeria", 36.2864, 7.9511),
        CityLocation("42 - تيبازة", "Tipaza", "Algeria", 36.5897, 2.4475),
        CityLocation("43 - ميلة", "Mila", "Algeria", 36.4503, 6.2644),
        CityLocation("44 - عين الدفلى", "Ain Defla", "Algeria", 36.2647, 1.9678),
        CityLocation("45 - النعامة", "Naama", "Algeria", 33.2667, -0.3167),
        CityLocation("46 - عين تموشنت", "Ain Temouchent", "Algeria", 35.2975, -1.1404),
        CityLocation("47 - غرداية", "Ghardaia", "Algeria", 32.4909, 3.6735),
        CityLocation("48 - غليزان", "Relizane", "Algeria", 35.7372, 0.5558)
    )

    // Holy Cities
    val holyCities = listOf(
        CityLocation("مكة المكرمة", "Makkah", "Saudi Arabia", 21.4225, 39.8262),
        CityLocation("المدينة المنورة", "Madinah", "Saudi Arabia", 24.4672, 39.6111),
        CityLocation("القدس الشريف", "Jerusalem", "Palestine", 31.7683, 35.2137)
    )

    val allCities = algerianCities + holyCities

    /**
     * Gets prayer timings 100% offline via astronomical solar calculation,
     * with fast online enhancement if connected.
     */
    suspend fun getPrayerTimings(city: CityLocation): PrayerTimings = withContext(Dispatchers.IO) {
        // Try network first (timeout 3.5s for fast offline transition)
        try {
            val dateStr = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date())
            val method = if (city.country == "Algeria") "3" else "4"
            val urlString = "https://api.aladhan.com/v1/timingsByCity/$dateStr?city=${city.englishName}&country=${city.country}&method=$method"
            val url = URL(urlString)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 3500
                readTimeout = 3500
                requestMethod = "GET"
            }

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                return@withContext parseTimingsResponse(response, dateStr)
            }
        } catch (_: Exception) {
            // Offline fallback
        }

        // 100% OFFLINE CALCULATION using precise latitude & longitude
        PrayerCalculator.calculate(
            latitude = city.latitude,
            longitude = city.longitude,
            date = Date(),
            fajrAngle = if (city.country == "Algeria") 18.0 else 18.5,
            ishaAngle = if (city.country == "Algeria") 17.0 else 18.5
        )
    }

    suspend fun getPrayerTimingsByCoordinates(latitude: Double, longitude: Double): Pair<PrayerTimings, String> = withContext(Dispatchers.IO) {
        try {
            val dateStr = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date())
            val urlString = "https://api.aladhan.com/v1/timings/$dateStr?latitude=$latitude&longitude=$longitude&method=3"
            val url = URL(urlString)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 3500
                readTimeout = 3500
                requestMethod = "GET"
            }

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val timings = parseTimingsResponse(response, dateStr)
                return@withContext Pair(timings, "موقعك الحالي بدقة GPS")
            }
        } catch (_: Exception) {
        }

        // Offline coordinates calculation
        val offlineTimings = PrayerCalculator.calculate(
            latitude = latitude,
            longitude = longitude,
            date = Date()
        )
        Pair(offlineTimings, "موقعك الحالي (حساب فلكي دقيق بدون نت)")
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
}
