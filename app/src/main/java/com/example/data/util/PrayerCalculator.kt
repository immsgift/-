package com.example.data.util

import com.example.data.model.PrayerTimings
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.*

/**
 * High-precision offline solar calculation for Islamic Prayer Times.
 * Implements the standard Muslim World League / Algerian Ministry of Religious Affairs method:
 * - Fajr angle: 18.0° (or 18.5° / MWL standard 18°)
 * - Isha angle: 17.0° (MWL standard 17°)
 * - Asr: Shafi'i / Hanbali / Maliki (Shadow factor = 1) - Maliki is the standard in Algeria and Maghreb.
 */
object PrayerCalculator {

    fun calculate(
        latitude: Double,
        longitude: Double,
        date: Date = Date(),
        fajrAngle: Double = 18.0,
        ishaAngle: Double = 17.0
    ): PrayerTimings {
        val calendar = Calendar.getInstance().apply { time = date }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Julian Day Number
        val jd = julianDate(year, month, day)

        // Time zone offset in hours
        val timeZoneOffset = calendar.timeZone.getOffset(date.time) / 3600000.0

        // Solar parameters
        val d = jd - 2451545.0
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sin(deg2rad(g)) + 0.020 * sin(deg2rad(2 * g)))

        val e = 23.439 - 0.00000036 * d
        val ra = rad2deg(atan2(cos(deg2rad(e)) * sin(deg2rad(l)), cos(deg2rad(l)))) / 15.0
        val rightAscension = fixHour(ra)

        val declination = rad2deg(asin(sin(deg2rad(e)) * sin(deg2rad(l))))
        val eqt = q / 15.0 - rightAscension

        // Noon / Dhuhr base (Solar midday in UTC)
        val noon = fixHour(12.0 - eqt - longitude / 15.0)

        // Sun altitude calculations
        fun sunAngleTime(angle: Double, isMorning: Boolean): Double {
            val sinAlt = sin(deg2rad(-angle))
            val cosAlt = cos(deg2rad(-angle))
            val cosH = (sinAlt - sin(deg2rad(latitude)) * sin(deg2rad(declination))) /
                    (cos(deg2rad(latitude)) * cos(deg2rad(declination)))

            if (cosH > 1.0 || cosH < -1.0) return Double.NaN
            val h = rad2deg(acos(cosH)) / 15.0
            return if (isMorning) noon - h else noon + h
        }

        // Asr (Maliki/Shafi'i shadow factor = 1)
        fun asrTime(): Double {
            val t = 1.0 + tan(deg2rad(abs(latitude - declination)))
            val angle = -rad2deg(atan(1.0 / t))
            val sinAlt = sin(deg2rad(angle))
            val cosH = (sinAlt - sin(deg2rad(latitude)) * sin(deg2rad(declination))) /
                    (cos(deg2rad(latitude)) * cos(deg2rad(declination)))
            if (cosH > 1.0 || cosH < -1.0) return Double.NaN
            val h = rad2deg(acos(cosH)) / 15.0
            return noon + h
        }

        // Sunrise and Sunset (approx 0.833° refraction)
        val sunriseUtc = sunAngleTime(0.833, true)
        val sunsetUtc = sunAngleTime(0.833, false)

        val fajrUtc = sunAngleTime(fajrAngle, true)
        val asrUtc = asrTime()
        val ishaUtc = sunAngleTime(ishaAngle, false)

        fun toLocalTime(utcHour: Double): String {
            if (utcHour.isNaN()) return "--:--"
            val localHour = fixHour(utcHour + timeZoneOffset)
            val h = localHour.toInt()
            val m = ((localHour - h) * 60.0).roundToInt().let { if (it == 60) 0 else it }
            val adjustedH = if (((localHour - h) * 60.0).roundToInt() == 60) (h + 1) % 24 else h
            return String.format(Locale.US, "%02d:%02d", adjustedH, m)
        }

        val gregorianStr = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(date)
        val hijriStr = calculateHijriDate(calendar)

        return PrayerTimings(
            fajr = toLocalTime(fajrUtc),
            sunrise = toLocalTime(sunriseUtc),
            dhuhr = toLocalTime(noon),
            asr = toLocalTime(asrUtc),
            maghrib = toLocalTime(sunsetUtc),
            isha = toLocalTime(ishaUtc),
            hijriDate = hijriStr,
            gregorianDate = gregorianStr
        )
    }

    private fun calculateHijriDate(cal: Calendar): String {
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH) + 1
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val jd = julianDate(y, m, d)
        
        // Approximate Islamic epoch Julian Day
        val l = jd - 1948440 + 10632
        val n = ((l - 1) / 10631).toInt()
        val l2 = l - 10631 * n + 354
        val j = (((10985 - l2) / 5316).toInt()) * (((50 * l2) / 17719).toInt()) +
                ((l2 / 5670).toInt()) * (((43 * l2) / 15238).toInt())
        val l3 = l2 - (((30 - j) / 15).toInt()) * (((17719 * j) / 50).toInt()) -
                ((j / 16).toInt()) * (((15238 * j) / 43).toInt()) + 29
        val hijriMonth = ((24 * l3) / 709).toInt()
        val hijriDay = (l3 - ((709 * hijriMonth) / 24).toInt()).toInt()
        val hijriYear = (30 * n + j - 30).toInt()

        val monthNamesAr = listOf(
            "محرم", "صفر", "ربيع الأول", "ربيع الثاني", "جمادى الأولى", "جمادى الآخرة",
            "رجب", "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
        )
        val monthAr = monthNamesAr.getOrElse(hijriMonth - 1) { "" }

        return "$hijriDay $monthAr $hijriYear هـ"
    }

    private fun julianDate(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun deg2rad(d: Double): Double = d * Math.PI / 180.0
    private fun rad2deg(r: Double): Double = r * 180.0 / Math.PI
    private fun fixHour(h: Double): Double = (h % 24.0 + 24.0) % 24.0
    private fun fixAngle(a: Double): Double = (a % 360.0 + 360.0) % 360.0
}
