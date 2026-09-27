import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import 'package:intl/intl.dart';

class PrayerTimes {
  final String fajr;
  final String sunrise;
  final String dhuhr;
  final String asr;
  final String maghrib;
  final String isha;
  final String hijriDate;
  final String gregorianDate;

  const PrayerTimes({
    required this.fajr,
    required this.sunrise,
    required this.dhuhr,
    required this.asr,
    required this.maghrib,
    required this.isha,
    required this.hijriDate,
    required this.gregorianDate,
  });

  factory PrayerTimes.fromJson(Map<String, dynamic> json) {
    final timings = json['timings'] as Map<String, dynamic>;
    final date = json['date'] as Map<String, dynamic>;
    final hijri = date['hijri'] as Map<String, dynamic>;
    final gregorian = date['gregorian'] as Map<String, dynamic>;

    String cleanTime(String raw) {
      // API returns like "05:12 (EEST)" or "05:12"
      return raw.split(' ').first;
    }

    final hijriDay = hijri['day'] ?? '';
    final hijriMonthAr = hijri['month']?['ar'] ?? hijri['month']?['en'] ?? '';
    final hijriYear = hijri['year'] ?? '';
    final fullHijri = '$hijriDay $hijriMonthAr $hijriYear هـ';

    final gregDate = gregorian['date'] ?? '';

    return PrayerTimes(
      fajr: cleanTime(timings['Fajr'] ?? '05:00'),
      sunrise: cleanTime(timings['Sunrise'] ?? '06:20'),
      dhuhr: cleanTime(timings['Dhuhr'] ?? '12:30'),
      asr: cleanTime(timings['Asr'] ?? '15:45'),
      maghrib: cleanTime(timings['Maghrib'] ?? '18:15'),
      isha: cleanTime(timings['Isha'] ?? '19:45'),
      hijriDate: fullHijri,
      gregorianDate: gregDate,
    );
  }

  static PrayerTimes get defaultFallback {
    final now = DateTime.now();
    final formatter = DateFormat('dd-MM-yyyy');
    return PrayerTimes(
      fajr: '05:10',
      sunrise: '06:30',
      dhuhr: '12:35',
      asr: '15:55',
      maghrib: '18:20',
      isha: '19:40',
      hijriDate: '١٤٤٧ هـ',
      gregorianDate: formatter.format(now),
    );
  }
}

class CityInfo {
  final String arabicName;
  final String englishName;
  final String country;

  const CityInfo(this.arabicName, this.englishName, this.country);
}

class PrayerService {
  static const List<CityInfo> popularCities = [
    CityInfo('مكة المكرمة', 'Makkah', 'Saudi Arabia'),
    CityInfo('المدينة المنورة', 'Madinah', 'Saudi Arabia'),
    CityInfo('الرياض', 'Riyadh', 'Saudi Arabia'),
    CityInfo('القاهرة', 'Cairo', 'Egypt'),
    CityInfo('القدس الشريف', 'Jerusalem', 'Palestine'),
    CityInfo('الجزائر العاصمة', 'Algiers', 'Algeria'),
    CityInfo('الرباط', 'Rabat', 'Morocco'),
    CityInfo('تونس', 'Tunis', 'Tunisia'),
    CityInfo('دبي', 'Dubai', 'United Arab Emirates'),
    CityInfo('عمان', 'Amman', 'Jordan'),
    CityInfo('بغداد', 'Baghdad', 'Iraq'),
    CityInfo('إسطنبول', 'Istanbul', 'Turkey'),
    CityInfo('باريس', 'Paris', 'France'),
    CityInfo('لندن', 'London', 'United Kingdom'),
  ];

  static Future<PrayerTimes> fetchPrayerTimes({
    String city = 'Makkah',
    String country = 'Saudi Arabia',
  }) async {
    try {
      final now = DateTime.now();
      final dateStr = DateFormat('dd-MM-yyyy').format(now);
      final url = Uri.parse(
        'https://api.aladhan.com/v1/timingsByCity/$dateStr?city=$city&country=$country&method=4',
      );

      final response = await http.get(url).timeout(const Duration(seconds: 7));
      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        if (data['code'] == 200 && data['data'] != null) {
          return PrayerTimes.fromJson(data['data']);
        }
      }
    } catch (e) {
      debugPrint('Error fetching prayer times: $e');
    }
    return PrayerTimes.defaultFallback;
  }
}
