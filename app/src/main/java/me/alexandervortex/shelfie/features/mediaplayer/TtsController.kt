package me.alexandervortex.shelfie.features.mediaplayer

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import me.alexandervortex.shelfie.features.mediaviewer.SpeechRate
import me.alexandervortex.shelfie.features.mediaviewer.TimerValue
import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.ui.model.ElementUI
import java.util.Locale
import java.util.concurrent.TimeUnit

class TtsController(
    context: Context,
    private val bookModel: BookUI?,
    private val errorAction: (String) -> Unit,
    private val scrollToIndex: (index: Int?, partIndex: Int?) -> Unit,
    private val onStateChanged: (Boolean) -> Unit = {},
    private val saveScrollState: (String, Int, Int) -> Unit = { _, _, _ -> },
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var isSpeaking = false
    private var stoppingTime: Long? = null
    private var currentIndex = 0
    private var currentPart = 0

    fun togglePlayPause(indexToStartPlaying: Int) {
        if (isSpeaking) {
            stopSpeaking()
        } else {
            startSpeaking(indexToStartPlaying)
        }
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
    }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) {
            errorAction("Ошибка инициализации TTS")
            return
        }

        val locale = bookModel?.titleInfo?.lang?.let(::Locale) ?: Locale.getDefault()
        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            errorAction("Язык не поддерживается")
        }
        initListener()
    }

    private fun initListener() {
        tts?.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    // формат: "el:part"
                    utteranceId?.split(':')?.let { parts ->
                        val el = parts.getOrNull(0)?.toIntOrNull()
                        val part = parts.getOrNull(1)?.toIntOrNull()
                        scrollToIndex(el, part)
                    }
                }

                override fun onDone(utteranceId: String?) {
                    if (isSpeaking) speakNext()
                }

                override fun onError(utteranceId: String?) {
                    errorAction("Ошибка TTS: $utteranceId")
                }
            }
        )
    }

    private fun startSpeaking(startIndex: Int) {
        val elements = bookModel?.elements.orEmpty()
        if (elements.isEmpty()) {
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
        val stopAt = stoppingTime
        if (stopAt != null && System.currentTimeMillis() >= stopAt) {
            bookModel?.let {
                saveScrollState.invoke(
                    it.titleInfo.id,
                    currentIndex,
                    currentPart
                )
            }

            stopSpeaking()
            return
        }

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
        tts?.stop()
        isSpeaking = false
        onStateChanged(isSpeaking)
    }

    fun updateSpeechRate(speed: SpeechRate) {
        tts?.setSpeechRate(speed.speed)
    }

    fun updateTimer(timer: TimerValue) {
        if (timer == TimerValue.OFF) {
            stoppingTime = null
            return
        }

        val now = System.currentTimeMillis()
        stoppingTime = now + TimeUnit.MINUTES.toMillis(timer.mins.toLong())
    }
}
