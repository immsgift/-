import 'dart:async';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../services/prayer_service.dart';
import '../theme/app_theme.dart';

class PrayerTimesScreen extends StatefulWidget {
  const PrayerTimesScreen({super.key});

  @override
  State<PrayerTimesScreen> createState() => _PrayerTimesScreenState();
}

class _PrayerTimesScreenState extends State<PrayerTimesScreen> {
  CityInfo _selectedCity = PrayerService.popularCities.first;
  PrayerTimes? _prayerTimes;
  bool _isLoading = true;
  Timer? _clockTimer;
  DateTime _currentTime = DateTime.now();

  @override
  void initState() {
    super.initState();
    _loadPrayerTimes();
    _clockTimer = Timer.periodic(const Duration(seconds: 1), (_) {
      if (mounted) {
        setState(() => _currentTime = DateTime.now());
      }
    });
  }

  @override
  void dispose() {
    _clockTimer?.cancel();
    super.dispose();
  }

  Future<void> _loadPrayerTimes() async {
    setState(() => _isLoading = true);
    final times = await PrayerService.fetchPrayerTimes(
      city: _selectedCity.englishName,
      country: _selectedCity.country,
    );
    if (mounted) {
      setState(() {
        _prayerTimes = times;
        _isLoading = false;
      });
    }
  }

  // Calculate next prayer and countdown
  Map<String, dynamic> _getNextPrayerInfo() {
    if (_prayerTimes == null) {
      return {'name': 'الفجر', 'time': '--:--', 'remaining': '--:--'};
    }

    final now = _currentTime;
    final prayers = [
      {'name': 'الفجر', 'time': _prayerTimes!.fajr, 'icon': Icons.wb_twilight},
      {'name': 'الشروق', 'time': _prayerTimes!.sunrise, 'icon': Icons.wb_sunny_outlined},
      {'name': 'الظهر', 'time': _prayerTimes!.dhuhr, 'icon': Icons.wb_sunny},
      {'name': 'العصر', 'time': _prayerTimes!.asr, 'icon': Icons.sunny_snowing},
      {'name': 'المغرب', 'time': _prayerTimes!.maghrib, 'icon': Icons.nights_stay_outlined},
      {'name': 'العشاء', 'time': _prayerTimes!.isha, 'icon': Icons.bedtime},
    ];

    for (final p in prayers) {
      final parts = (p['time'] as String).split(':');
      if (parts.length >= 2) {
        final hour = int.tryParse(parts[0]) ?? 0;
        final min = int.tryParse(parts[1]) ?? 0;
        final prayerDt = DateTime(now.year, now.month, now.day, hour, min);

        if (prayerDt.isAfter(now)) {
          final diff = prayerDt.difference(now);
          final hoursLeft = diff.inHours;
          final minsLeft = diff.inMinutes % 60;
          final secsLeft = diff.inSeconds % 60;
          final formattedDiff =
              '${hoursLeft > 0 ? '$hoursLeft ساعة و ' : ''}$minsLeft دقيقة و $secsLeft ثانية';
          return {
            'name': p['name'],
            'time': p['time'],
            'remaining': formattedDiff,
            'icon': p['icon'],
          };
        }
      }
    }

    // Next is tomorrow's Fajr
    return {
      'name': 'الفجر (غداً)',
      'time': _prayerTimes!.fajr,
      'remaining': 'صلاة الفجر القادمة',
      'icon': Icons.wb_twilight,
    };
  }

  @override
  Widget build(BuildContext context) {
    final nextPrayer = _getNextPrayerInfo();
    final timeFormatter = DateFormat('hh:mm:ss a');
    final formattedNow = timeFormatter.format(_currentTime);

    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'مواقيت الصلاة',
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
        ),
        centerTitle: true,
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            tooltip: 'تحديث المواقيت',
            onPressed: _loadPrayerTimes,
          ),
        ],
      ),
      body: _isLoading
          ? const Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircularProgressIndicator(color: AppColors.emeraldPrimary),
                  SizedBox(height: 16),
                  Text('جارٍ جلب مواقيت الصلاة...'),
                ],
              ),
            )
          : RefreshIndicator(
              onRefresh: _loadPrayerTimes,
              color: AppColors.emeraldPrimary,
              child: ListView(
                padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                children: [
                  // City Selector Card
                  Card(
                    elevation: 1,
                    child: Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
                      child: Row(
                        children: [
                          const Icon(Icons.location_on, color: AppColors.emeraldPrimary),
                          const SizedBox(width: 10),
                          const Text(
                            'المدينة:',
                            style: TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
                          ),
                          const Spacer(),
                          DropdownButtonHideUnderline(
                            child: DropdownButton<CityInfo>(
                              value: _selectedCity,
                              icon: const Icon(Icons.keyboard_arrow_down, color: AppColors.emeraldPrimary),
                              items: PrayerService.popularCities.map((city) {
                                return DropdownMenuItem(
                                  value: city,
                                  child: Text(
                                    city.arabicName,
                                    style: const TextStyle(fontWeight: FontWeight.w600),
                                  ),
                                );
                              }).toList(),
                              onChanged: (newCity) {
                                if (newCity != null) {
                                  setState(() => _selectedCity = newCity);
                                  _loadPrayerTimes();
                                }
                              },
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),

                  const SizedBox(height: 12),

                  // Hero Countdown Card
                  Container(
                    padding: const EdgeInsets.all(20),
                    decoration: BoxDecoration(
                      gradient: const LinearGradient(
                        colors: [AppColors.emeraldDark, AppColors.emeraldPrimary],
                        begin: Alignment.topRight,
                        end: Alignment.bottomLeft,
                      ),
                      borderRadius: BorderRadius.circular(22),
                      boxShadow: [
                        BoxShadow(
                          color: AppColors.emeraldDark.withOpacity(0.3),
                          blurRadius: 10,
                          offset: const Offset(0, 4),
                        ),
                      ],
                    ),
                    child: Column(
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              _prayerTimes?.hijriDate ?? '',
                              style: const TextStyle(color: AppColors.goldLight, fontSize: 13),
                            ),
                            Text(
                              formattedNow,
                              style: const TextStyle(color: Colors.white70, fontSize: 13),
                            ),
                          ],
                        ),
                        const SizedBox(height: 16),
                        Container(
                          padding: const EdgeInsets.all(12),
                          decoration: BoxDecoration(
                            color: Colors.white.withOpacity(0.12),
                            shape: BoxShape.circle,
                          ),
                          child: Icon(
                            nextPrayer['icon'] as IconData? ?? Icons.access_time,
                            color: AppColors.goldAccent,
                            size: 38,
                          ),
                        ),
                        const SizedBox(height: 10),
                        Text(
                          'الصلاة القادمة: ${nextPrayer['name']}',
                          style: const TextStyle(
                            color: Colors.white,
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        const SizedBox(height: 4),
                        Text(
                          nextPrayer['time'] as String,
                          style: const TextStyle(
                            color: AppColors.goldLight,
                            fontSize: 34,
                            fontWeight: FontWeight.w900,
                            letterSpacing: 2,
                          ),
                        ),
                        const SizedBox(height: 8),
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 6),
                          decoration: BoxDecoration(
                            color: Colors.black26,
                            borderRadius: BorderRadius.circular(20),
                          ),
                          child: Text(
                            'متبقي: ${nextPrayer['remaining']}',
                            style: const TextStyle(color: Colors.white, fontSize: 12),
                          ),
                        ),
                      ],
                    ),
                  ),

                  const SizedBox(height: 20),

                  const Padding(
                    padding: EdgeInsets.symmetric(horizontal: 4, vertical: 4),
                    child: Text(
                      'أوقات الصلوات الخمس',
                      style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                    ),
                  ),

                  const SizedBox(height: 8),

                  // Prayer times items
                  _buildPrayerTile(
                    title: 'صلاة الفجر',
                    time: _prayerTimes?.fajr ?? '--:--',
                    icon: Icons.wb_twilight,
                    isNext: nextPrayer['name'] == 'الفجر',
                  ),
                  _buildPrayerTile(
                    title: 'الشروق',
                    time: _prayerTimes?.sunrise ?? '--:--',
                    icon: Icons.wb_sunny_outlined,
                    isNext: nextPrayer['name'] == 'الشروق',
                    isSecondary: true,
                  ),
                  _buildPrayerTile(
                    title: 'صلاة الظهر',
                    time: _prayerTimes?.dhuhr ?? '--:--',
                    icon: Icons.wb_sunny,
                    isNext: nextPrayer['name'] == 'الظهر',
                  ),
                  _buildPrayerTile(
                    title: 'صلاة العصر',
                    time: _prayerTimes?.asr ?? '--:--',
                    icon: Icons.sunny_snowing,
                    isNext: nextPrayer['name'] == 'العصر',
                  ),
                  _buildPrayerTile(
                    title: 'صلاة المغرب',
                    time: _prayerTimes?.maghrib ?? '--:--',
                    icon: Icons.nights_stay_outlined,
                    isNext: nextPrayer['name'] == 'المغرب',
                  ),
                  _buildPrayerTile(
                    title: 'صلاة العشاء',
                    time: _prayerTimes?.isha ?? '--:--',
                    icon: Icons.bedtime,
                    isNext: nextPrayer['name'] == 'العشاء',
                  ),

                  const SizedBox(height: 20),

                  // Hadith on prayer card
                  Container(
                    padding: const EdgeInsets.all(16),
                    decoration: BoxDecoration(
                      color: AppColors.emeraldContainer.withOpacity(0.4),
                      borderRadius: BorderRadius.circular(16),
                      border: Border.all(color: AppColors.goldAccent.withOpacity(0.3)),
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Row(
                          children: [
                            Icon(Icons.format_quote, color: AppColors.emeraldPrimary, size: 20),
                            SizedBox(width: 6),
                            Text(
                              'فضل الصلاة في وقتها',
                              style: TextStyle(
                                fontWeight: FontWeight.bold,
                                color: AppColors.emeraldPrimary,
                                fontSize: 14,
                              ),
                            ),
                          ],
                        ),
                        const SizedBox(height: 8),
                        Text(
                          'سُئل النبي ﷺ: أي الأعمال أحب إلى الله؟ قال: «الصلاة على وقتها». (متفق عليه)',
                          style: TextStyle(
                            fontSize: 13,
                            color: Colors.grey.shade800,
                            height: 1.5,
                          ),
                        ),
                      ],
                    ),
                  ),

                  const SizedBox(height: 80),
                ],
              ),
            ),
    );
  }

  Widget _buildPrayerTile({
    required String title,
    required String time,
    required IconData icon,
    required bool isNext,
    bool isSecondary = false,
  }) {
    return Container(
      margin: const EdgeInsets.only(bottom: 8),
      decoration: BoxDecoration(
        color: isNext
            ? AppColors.emeraldPrimary.withOpacity(0.08)
            : Theme.of(context).cardTheme.color,
        borderRadius: BorderRadius.circular(14),
        border: Border.all(
          color: isNext ? AppColors.emeraldPrimary : Colors.black12,
          width: isNext ? 1.5 : 0.8,
        ),
      ),
      child: ListTile(
        leading: Container(
          padding: const EdgeInsets.all(8),
          decoration: BoxDecoration(
            color: isNext
                ? AppColors.emeraldPrimary
                : (isSecondary ? Colors.orange.shade50 : AppColors.emeraldContainer),
            shape: BoxShape.circle,
          ),
          child: Icon(
            icon,
            size: 20,
            color: isNext
                ? Colors.white
                : (isSecondary ? Colors.orange.shade800 : AppColors.emeraldPrimary),
          ),
        ),
        title: Text(
          title,
          style: TextStyle(
            fontWeight: isNext ? FontWeight.bold : FontWeight.w600,
            fontSize: 15,
            color: isNext ? AppColors.emeraldPrimary : null,
          ),
        ),
        trailing: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            if (isNext)
              Container(
                margin: const EdgeInsets.only(left: 8),
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                decoration: BoxDecoration(
                  color: AppColors.emeraldPrimary,
                  borderRadius: BorderRadius.circular(10),
                ),
                child: const Text(
                  'التالية',
                  style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold),
                ),
              ),
            Text(
              time,
              style: TextStyle(
                fontSize: 17,
                fontWeight: FontWeight.bold,
                color: isNext ? AppColors.emeraldPrimary : null,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
