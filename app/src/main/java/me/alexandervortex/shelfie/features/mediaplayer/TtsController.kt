package me.alexandervortex.shelfie.features.mediaplayer

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import me.alexandervortex.shelfie.features.viewer.TAG
import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.ui.model.ElementUI
import java.util.Locale

class TtsController(
    context: Context,
    private val bookModel: BookUI?,
    private val errorAction: (String) -> Unit,
    private val scrollToIndex: (index: Int?, partIndex: Int?) -> Unit,
    private val onStateChanged: (Boolean) -> Unit = {},
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var isSpeaking = false

    private var currentIndex = 0
    private var currentPart = 0

    fun togglePlayPause(indexToStartPlaying: Int) {
        Log.d("${TAG}_TtsController", "togglePlayPause:${indexToStartPlaying}:${isSpeaking}")
        if (isSpeaking) {
            stopSpeaking()
        } else {
            startSpeaking(indexToStartPlaying)
        }
    }

    fun release() {
        Log.d("${TAG}_TtsController", "release")
        tts?.stop()
        tts?.shutdown()
    }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) {
            Log.d("${TAG}_TtsController", "Ошибка инициализации TTS")
            errorAction("Ошибка инициализации TTS")
            return
        }

        val locale = bookModel?.titleInfo?.lang?.let(::Locale) ?: Locale.getDefault()
        Log.d("${TAG}_TtsController", "onInit:$status TextToSpeech.SUCCESS")
        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            errorAction("Язык не поддерживается")
            Log.e("${TAG}_TtsController", "Язык не поддерживается")
        }

        tts?.setSpeechRate(4f)
        tts?.setPitch(0.8f)
        initListener()
    }

    private fun initListener() {
        tts?.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    // формат: "el:part"
                    Log.d("${TAG}_TtsController", "onStart:$utteranceId")
                    utteranceId?.split(':')?.let { parts ->
                        val el = parts.getOrNull(0)?.toIntOrNull()
                        val part = parts.getOrNull(1)?.toIntOrNull()
                        scrollToIndex(el, part)
                    }
                }

                override fun onDone(utteranceId: String?) {
                    Log.d("${TAG}_TtsController", "onDone:$utteranceId")
                    if (isSpeaking) speakNext()
                }

                override fun onError(utteranceId: String?) {
                    Log.d("${TAG}_TtsController", "Ошибка TTS: $utteranceId")
                    errorAction("Ошибка TTS: $utteranceId")
                }
            }
        )
    }

    private fun startSpeaking(startIndex: Int) {
        Log.d("${TAG}_TtsController", "startSpeaking:$startIndex")
        val elements = bookModel?.elements.orEmpty()
        if (elements.isEmpty()) {
            Log.d("${TAG}_TtsController", "BookModel elements are null or empty")
            errorAction("Пустая книга")
            return
        }

        isSpeaking = true
        onStateChanged(isSpeaking)
        currentIndex = startIndex
        currentPart = 0
        tts?.stop()
        speakNext()
    }

    private fun speakNext() {
        val elements = bookModel?.elements ?: return stopSpeaking()
        if (currentIndex >= elements.size) return stopSpeaking()

        val textUi = elements[currentIndex] as? ElementUI.TextUI ?: run {
            currentIndex++
            return speakNext()
        }

        val parts = textUi.parts
        if (currentPart >= parts.size) {
            currentIndex++
            currentPart = 0
            return speakNext()
        }

        val sentence = parts[currentPart].trim()
        if (sentence.isBlank()) {
            currentPart++
            return speakNext()
        }

        val utteranceId = "$currentIndex:$currentPart"
        currentPart++
        tts?.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    private fun stopSpeaking() {
        Log.d("${TAG}_TtsController", "stopSpeaking")
        tts?.stop()
        isSpeaking = false
        onStateChanged(isSpeaking)
    }
}
