package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

data class AudioPlaybackState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val title: String = "",
    val subtitle: String = "",
    val currentPositionMs: Int = 0,
    val durationMs: Int = 0,
    val activeSurahId: Int? = null,
    val activeAyahNumber: Int? = null,
    val activeDhikrId: String? = null
)

class QuranAudioManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var textToSpeech: TextToSpeech? = null
    private var isTtsReady = false

    private val _playbackState = MutableStateFlow(AudioPlaybackState())
    val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Main)
    private var progressTrackerJob: Job? = null

    init {
        initTts()
    }

    private fun initTts() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale("ar"))
                isTtsReady = (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED)
                textToSpeech?.setSpeechRate(0.88f) // Reverent, clear recitation pace
            }
        }

        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _playbackState.value = _playbackState.value.copy(
                    isPlaying = true,
                    isBuffering = false
                )
            }

            override fun onDone(utteranceId: String?) {
                _playbackState.value = _playbackState.value.copy(
                    isPlaying = false,
                    activeDhikrId = null
                )
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _playbackState.value = _playbackState.value.copy(
                    isPlaying = false,
                    isBuffering = false,
                    activeDhikrId = null
                )
            }
        })
    }

    /**
     * Recites a Dhikr or Dua aloud using TTS.
     */
    fun reciteDhikr(dhikrId: String, title: String, text: String, onFinished: (() -> Unit)? = null) {
        stopAll()

        _playbackState.value = AudioPlaybackState(
            isPlaying = true,
            isBuffering = false,
            title = title,
            subtitle = "تلاوة واستماع للأذكار والأدعية",
            activeDhikrId = dhikrId
        )

        val cleanText = text.replace("۝", ". ")
        val params = android.os.Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, dhikrId)

        textToSpeech?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, params, dhikrId)
    }

    /**
     * Plays recitation audio for an Ayah or Surah via HTTP stream.
     */
    fun playQuranAudio(
        surahId: Int,
        ayahNumber: Int,
        surahName: String,
        reciterFolder: String = "Alafasy_128kbps",
        onAyahCompleted: (() -> Unit)? = null
    ) {
        stopAll()

        val surahFormatted = String.format(Locale.US, "%03d", surahId)
        val ayahFormatted = String.format(Locale.US, "%03d", ayahNumber)
        val audioUrl = "https://everyayah.com/data/$reciterFolder/$surahFormatted$ayahFormatted.mp3"

        _playbackState.value = AudioPlaybackState(
            isPlaying = true,
            isBuffering = true,
            title = "سورة $surahName",
            subtitle = "الآية رقم $ayahNumber",
            activeSurahId = surahId,
            activeAyahNumber = ayahNumber
        )

        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(audioUrl)
                setOnPreparedListener { mp ->
                    mp.start()
                    _playbackState.value = _playbackState.value.copy(
                        isPlaying = true,
                        isBuffering = false,
                        durationMs = mp.duration
                    )
                    startProgressTracker()
                }
                setOnCompletionListener {
                    _playbackState.value = _playbackState.value.copy(
                        isPlaying = false,
                        currentPositionMs = 0
                    )
                    progressTrackerJob?.cancel()
                    onAyahCompleted?.invoke()
                }
                setOnErrorListener { _, _, _ ->
                    _playbackState.value = _playbackState.value.copy(
                        isPlaying = false,
                        isBuffering = false
                    )
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            _playbackState.value = _playbackState.value.copy(
                isPlaying = false,
                isBuffering = false
            )
        }
    }

    fun pause() {
        if (textToSpeech?.isSpeaking == true) {
            textToSpeech?.stop()
        }
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
        _playbackState.value = _playbackState.value.copy(isPlaying = false)
        progressTrackerJob?.cancel()
    }

    fun resume() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                _playbackState.value = _playbackState.value.copy(isPlaying = true)
                startProgressTracker()
            }
        }
    }

    fun stopAll() {
        progressTrackerJob?.cancel()
        try {
            textToSpeech?.stop()
        } catch (_: Exception) {}

        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
                it.release()
            }
            mediaPlayer = null
        } catch (_: Exception) {}

        _playbackState.value = AudioPlaybackState(isPlaying = false)
    }

    private fun startProgressTracker() {
        progressTrackerJob?.cancel()
        progressTrackerJob = scope.launch {
            while (isActive) {
                mediaPlayer?.let { mp ->
                    if (mp.isPlaying) {
                        _playbackState.value = _playbackState.value.copy(
                            currentPositionMs = mp.currentPosition,
                            durationMs = mp.duration
                        )
                    }
                }
                delay(500)
            }
        }
    }

    fun release() {
        stopAll()
        try {
            textToSpeech?.shutdown()
            textToSpeech = null
        } catch (_: Exception) {}
    }
}
