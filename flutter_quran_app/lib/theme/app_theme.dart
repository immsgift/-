import 'package:flutter/material.dart';

class AppColors {
  static const Color emeraldPrimary = Color(0xFF0F613D);
  static const Color emeraldDark = Color(0xFF093E26);
  static const Color emeraldContainer = Color(0xFFD6F0E2);
  static const Color goldAccent = Color(0xFFC9971D);
  static const Color goldLight = Color(0xFFF9E8B8);
  static const Color parchmentLight = Color(0xFFFAF7EF);
  static const Color parchmentDark = Color(0xFF141C18);
  static const Color surfaceDark = Color(0xFF1C2721);
}

class AppTheme {
  static ThemeData get lightTheme {
    return ThemeData(
      useMaterial3: true,
      brightness: Brightness.light,
      primaryColor: AppColors.emeraldPrimary,
      scaffoldBackgroundColor: AppColors.parchmentLight,
      colorScheme: ColorScheme.fromSeed(
        seedColor: AppColors.emeraldPrimary,
        primary: AppColors.emeraldPrimary,
        secondary: AppColors.goldAccent,
        surface: Colors.white,
        background: AppColors.parchmentLight,
        brightness: Brightness.light,
      ),
      appBarTheme: const AppBarTheme(
        backgroundColor: AppColors.emeraldDark,
        foregroundColor: Colors.white,
        elevation: 0,
      ),
      cardTheme: CardTheme(
        color: Colors.white,
        elevation: 1,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      ),
    );
  }

  static ThemeData get darkTheme {
    return ThemeData(
      useMaterial3: true,
      brightness: Brightness.dark,
      primaryColor: AppColors.emeraldPrimary,
      scaffoldBackgroundColor: AppColors.parchmentDark,
      colorScheme: ColorScheme.fromSeed(
        seedColor: AppColors.emeraldPrimary,
        primary: AppColors.goldAccent,
        secondary: AppColors.goldAccent,
        surface: AppColors.surfaceDark,
        background: AppColors.parchmentDark,
        brightness: Brightness.dark,
      ),
      appBarTheme: const AppBarTheme(
        backgroundColor: AppColors.surfaceDark,
        foregroundColor: Colors.white,
        elevation: 0,
      ),
      cardTheme: CardTheme(
        color: AppColors.surfaceDark,
        elevation: 1,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      ),
    );
  }
}
