# Flutter Quran & Adhkar App (iOS & Android)

A complete, production-ready cross-platform Holy Quran, Adhkar, and Duas application built with **Flutter 3** and **Dart**, featuring background audio playback with lock-screen media controls for both iOS and Android.

## Features
- **Holy Quran Index & Reader:** Complete catalog of all 114 Surahs, Uthmani script, font scaling dialog, translations, and tafsir.
- **Audio Recitations with Background Lockscreen Playback:** Powered by `just_audio` and `just_audio_background` (`audio_service` + `audio_session`) supporting lock-screen play/pause/seek controls on iOS and notification media controls on Android.
- **Adhkar & Duas (الأذكار والأدعية):** Daily Adhkar (Morning, Evening, Sleep, Post-prayer) and Occasion Duas (Quran Khatm, Travel, Distress relief, Ruqyah, Istikhara) with audio recitation and interactive tap counters.
- **Digital Tasbih (السبحة الإلكترونية):** Smart haptic counter with 33/100/free targets.
- **Bookmarks & Khatmah Plan:** Reading tracker and saved verses.
- **Islamic Emerald & Gold UI:** Full Material 3 theming with native Arabic RTL support.

## Project Structure
```
flutter_quran_app/
├── pubspec.yaml
├── ios/
│   ├── Podfile
│   └── Runner/Info.plist
├── android/
│   └── app/src/main/AndroidManifest.xml
└── lib/
    ├── main.dart
    ├── theme/app_theme.dart
    ├── models/
    │   ├── surah.dart
    │   ├── ayah.dart
    │   └── adhkar.dart
    ├── services/
    │   ├── quran_service.dart
    │   └── audio_player_service.dart
    ├── screens/
    │   ├── home_screen.dart
    │   ├── surah_reader_screen.dart
    │   ├── adhkar_screen.dart
    │   ├── tasbih_screen.dart
    │   └── bookmarks_screen.dart
    └── widgets/
        └── mini_player.dart
```

## Running the App

### 1. Install dependencies
```bash
cd flutter_quran_app
flutter pub get
```

### 2. iOS Setup (Mac)
```bash
cd ios
pod install
cd ..
flutter run -d ios
```

### 3. Android Setup
```bash
flutter run -d android
```
