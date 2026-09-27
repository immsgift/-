package com.example.data.model

data class PrayerTimings(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val hijriDate: String,
    val gregorianDate: String
)

data class CityLocation(
    val arabicName: String,
    val englishName: String,
    val country: String
)
