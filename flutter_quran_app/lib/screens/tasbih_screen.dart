import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../theme/app_theme.dart';

class TasbihScreen extends StatefulWidget {
  const TasbihScreen({super.key});

  @override
  State<TasbihScreen> createState() => _TasbihScreenState();
}

class _TasbihScreenState extends State<TasbihScreen> {
  int _counter = 0;
  int _target = 33;
  int _laps = 0;
  int _total = 0;
  String _selectedPhrase = 'سُبْحَانَ اللَّهِ';

  static const List<String> phrases = [
    'سُبْحَانَ اللَّهِ',
    'الْحَمْدُ لِلَّهِ',
    'لَا إِلَٰهَ إِلَّا اللَّهُ',
    'اللَّهُ أَكْبَرُ',
    'أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ',
    'لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ',
    'اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ',
  ];

  void _increment() {
    HapticFeedback.lightImpact();
    setState(() {
      _counter++;
      _total++;
      if (_target > 0 && _counter >= _target) {
        _counter = 0;
        _laps++;
        HapticFeedback.heavyImpact();
      }
    });
  }

  void _reset() {
    setState(() {
      _counter = 0;
      _laps = 0;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('السبحة الإلكترونية', style: TextStyle(fontWeight: FontWeight.bold)),
      ),
      body: Column(
        children: [
          // Phrases selector
          Container(
            height: 50,
            margin: const EdgeInsets.symmetric(vertical: 10),
            child: ListView.builder(
              scrollDirection: Axis.horizontal,
              padding: const EdgeInsets.symmetric(horizontal: 14),
              itemCount: phrases.length,
              itemBuilder: (context, index) {
                final p = phrases[index];
                final isSelected = p == _selectedPhrase;
                return Padding(
                  padding: const EdgeInsets.only(right: 8),
                  child: FilterChip(
                    selected: isSelected,
                    label: Text(p),
                    selectedColor: AppColors.emeraldPrimary,
                    labelStyle: TextStyle(
                      color: isSelected ? Colors.white : Colors.black87,
                      fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
                    ),
                    onSelected: (_) => setState(() {
                      _selectedPhrase = p;
                      _counter = 0;
                    }),
                  ),
                );
              },
            ),
          ),

          // Target selector
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              _buildTargetChip(33, '٣٣ تسبيحة'),
              const SizedBox(width: 8),
              _buildTargetChip(100, '١٠٠ تسبيحة'),
              const SizedBox(width: 8),
              _buildTargetChip(0, 'حر'),
            ],
          ),

          const Spacer(),

          // Active phrase
          Text(
            _selectedPhrase,
            style: const TextStyle(fontSize: 26, fontWeight: FontWeight.bold),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 24),

          // Big circular tap target
          GestureDetector(
            onTap: _increment,
            child: Container(
              width: 220,
              height: 220,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                gradient: const RadialGradient(
                  colors: [AppColors.emeraldPrimary, AppColors.emeraldDark],
                ),
                border: Border.all(color: AppColors.goldAccent, width: 4),
                boxShadow: [
                  BoxShadow(
                    color: AppColors.emeraldPrimary.withOpacity(0.3),
                    blurRadius: 20,
                    spreadRadius: 4,
                  ),
                ],
              ),
              alignment: Alignment.center,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(
                    '$_counter',
                    style: const TextStyle(
                      fontSize: 56,
                      fontWeight: FontWeight.bold,
                      color: Colors.white,
                    ),
                  ),
                  Text(
                    _target > 0 ? 'الهدف: $_target' : 'تسبيح حر',
                    style: const TextStyle(color: AppColors.goldLight, fontSize: 14),
                  ),
                  const SizedBox(height: 6),
                  const Text(
                    'اضغط للتسبيح',
                    style: TextStyle(color: Colors.white70, fontSize: 11),
                  ),
                ],
              ),
            ),
          ),

          const Spacer(),

          // Stats and reset
          Container(
            margin: const EdgeInsets.all(20),
            padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
            decoration: BoxDecoration(
              color: Theme.of(context).cardTheme.color,
              borderRadius: BorderRadius.circular(16),
            ),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                Column(
                  children: [
                    const Text('الدورات', style: TextStyle(fontSize: 12, color: Colors.grey)),
                    Text(
                      '$_laps',
                      style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: AppColors.emeraldPrimary),
                    ),
                  ],
                ),
                Column(
                  children: [
                    const Text('المجموع الكلي', style: TextStyle(fontSize: 12, color: Colors.grey)),
                    Text(
                      '$_total',
                      style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: AppColors.emeraldPrimary),
                    ),
                  ],
                ),
                IconButton(
                  icon: const Icon(Icons.refresh, color: Colors.redAccent),
                  tooltip: 'إعادة ضبط',
                  onPressed: _reset,
                ),
              ],
            ),
          ),
          const SizedBox(height: 60),
        ],
      ),
    );
  }

  Widget _buildTargetChip(int target, String label) {
    final isSelected = _target == target;
    return ChoiceChip(
      selected: isSelected,
      label: Text(label),
      selectedColor: AppColors.goldAccent,
      labelStyle: TextStyle(
        color: isSelected ? Colors.black : Colors.black87,
        fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
      ),
      onSelected: (_) => setState(() {
        _target = target;
        _counter = 0;
      }),
    );
  }
}
