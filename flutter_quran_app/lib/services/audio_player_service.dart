import 'package:flutter/foundation.dart';
import 'package:just_audio/just_audio.dart';
import 'package:just_audio_background/just_audio_background.dart';
import 'package:audio_session/audio_session.dart';

class QuranAudioPlayerService extends ChangeNotifier {
  final AudioPlayer _player = AudioPlayer();

  bool _isPlaying = false;
  bool _isBuffering = false;
  String _currentTitle = '';
  String _currentSubtitle = '';
  Duration _position = Duration.zero;
  Duration _duration = Duration.zero;
  int? _activeSurahId;
  int? _activeAyahNumber;

  bool get isPlaying => _isPlaying;
  bool get isBuffering => _isBuffering;
  String get currentTitle => _currentTitle;
  String get currentSubtitle => _currentSubtitle;
  Duration get position => _position;
  Duration get duration => _duration;
  int? get activeSurahId => _activeSurahId;
  int? get activeAyahNumber => _activeAyahNumber;

  QuranAudioPlayerService() {
    _initAudioSession();
    _listenToPlayerState();
  }

  Future<void> _initAudioSession() async {
    // Configure iOS & Android audio session for background playback and lockscreen
    final session = await AudioSession.instance;
    await session.configure(const AudioSessionConfiguration.music());
  }

  void _listenToPlayerState() {
    _player.playerStateStream.listen((state) {
      _isPlaying = state.playing;
      _isBuffering = state.processingState == ProcessingState.buffering ||
          state.processingState == ProcessingState.loading;
      notifyListeners();
    });

    _player.positionStream.listen((pos) {
      _position = pos;
      notifyListeners();
    });

    _player.durationStream.listen((dur) {
      _duration = dur ?? Duration.zero;
      notifyListeners();
    });
  }

  /// Play Quran Ayah Audio with lock-screen notification metadata
  Future<void> playAyahAudio({
    required int surahId,
    required int ayahNumber,
    required String surahName,
    String reciterFolder = 'Alafasy_128kbps',
    String reciterName = 'مشاري راشد العفاسي',
  }) async {
    try {
      final s = surahId.toString().padLeft(3, '0');
      final a = ayahNumber.toString().padLeft(3, '0');
      final url = 'https://everyayah.com/data/$reciterFolder/$s$a.mp3';

      _currentTitle = 'سورة $surahName';
      _currentSubtitle = 'الآية $ayahNumber • $reciterName';
      _activeSurahId = surahId;
      _activeAyahNumber = ayahNumber;
      notifyListeners();

      // AudioSource with background lockscreen MediaItem metadata
      final audioSource = AudioSource.uri(
        Uri.parse(url),
        tag: MediaItem(
          id: '$surahId-$ayahNumber',
          album: 'القرآن الكريم',
          title: 'سورة $surahName - الآية $ayahNumber',
          artist: reciterName,
          artUri: Uri.parse('https://cdn-icons-png.flaticon.com/512/3655/3655598.png'),
        ),
      );

      await _player.setAudioSource(audioSource);
      await _player.play();
    } catch (e) {
      if (kDebugMode) {
        print('Error playing ayah audio: $e');
      }
    }
  }

  /// Play any audio stream URL (for Adhkar or custom streams)
  Future<void> playCustomAudio({
    required String url,
    required String title,
    required String subtitle,
  }) async {
    try {
      _currentTitle = title;
      _currentSubtitle = subtitle;
      notifyListeners();

      final audioSource = AudioSource.uri(
        Uri.parse(url),
        tag: MediaItem(
          id: url,
          album: 'الأذكار والأدعية',
          title: title,
          artist: subtitle,
          artUri: Uri.parse('https://cdn-icons-png.flaticon.com/512/3655/3655598.png'),
        ),
      );

      await _player.setAudioSource(audioSource);
      await _player.play();
    } catch (e) {
      if (kDebugMode) {
        print('Error playing audio: $e');
      }
    }
  }

  Future<void> pause() async {
    await _player.pause();
  }

  Future<void> resume() async {
    await _player.play();
  }

  Future<void> stop() async {
    await _player.stop();
    _currentTitle = '';
    _currentSubtitle = '';
    _activeSurahId = null;
    _activeAyahNumber = null;
    notifyListeners();
  }

  Future<void> seek(Duration position) async {
    await _player.seek(position);
  }

  @override
  void dispose() {
    _player.dispose();
    super.dispose();
  }
}
