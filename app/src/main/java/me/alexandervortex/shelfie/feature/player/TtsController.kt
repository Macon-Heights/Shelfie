package me.alexandervortex.shelfie.feature.player

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import me.alexandervortex.shelfie.feature.settings.values.SpeechRateValue
import me.alexandervortex.shelfie.feature.settings.values.TimerValue
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.UI
import java.util.Locale
import java.util.concurrent.TimeUnit

class TtsController(
    context: Context,
    private val bookModel: BookUIModel?,
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
            errorAction("Error while initializing TTS")
            return
        }

        val locale = bookModel?.titleInfo?.lang?.let { lang ->
            Locale.forLanguageTag(lang)
        } ?: Locale.getDefault()

        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            errorAction("Language not supported")
        }
        initListener()
    }

    private fun initListener() {
        tts?.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    // format: "el:part"
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
                    errorAction("TTS Error: $utteranceId")
                }
            }
        )
    }

    private fun startSpeaking(startIndex: Int) {
        val elements = bookModel?.elements.orEmpty()
        if (elements.isEmpty()) {
            errorAction("Empty Book")
            return
        }

        isSpeaking = true
        onStateChanged(isSpeaking)
        currentIndex = startIndex
        currentPart = 0

        bookModel?.let {
            saveScrollState.invoke(it.id, currentIndex, currentPart)
        }

        tts?.stop()
        speakNext()
    }

    fun changePlayPosition(step: Int) {
        val elements = bookModel?.elements.orEmpty()
        if (elements.isEmpty()) return

        tts?.stop()

        currentIndex = (currentIndex + step).coerceIn(0, elements.lastIndex)
        currentPart = 0

        bookModel?.let {
            saveScrollState.invoke(it.id, currentIndex, currentPart)
        }

        if (isSpeaking) {
            speakNext()
        } else {
            scrollToIndex(currentIndex, currentPart)
        }
    }

    private fun speakNext() {
        val model = bookModel ?: return stopSpeaking()
        val stopAt = stoppingTime
        if (stopAt != null && System.currentTimeMillis() >= stopAt) {
            saveScrollState.invoke(
                model.id,
                currentIndex,
                currentPart
            )

            stopSpeaking()
            return
        }

        val elements = model.elements
        if (currentIndex >= elements.size) return stopSpeaking()

        val textUi = elements[currentIndex] as? UI.ComplexText ?: run {
            currentIndex++
            saveScrollState.invoke(model.id, currentIndex, 0)
            return speakNext()
        }

        val parts = textUi.parts
        if (currentPart >= parts.size) {
            currentIndex++
            currentPart = 0
            saveScrollState.invoke(model.id, currentIndex, currentPart)
            return speakNext()
        }

        val sentence = parts[currentPart].text.trim()
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
        bookModel?.let {
            saveScrollState.invoke(it.id, currentIndex, currentPart)
        }
        onStateChanged(isSpeaking)
    }

    fun updateSpeechRate(speed: SpeechRateValue) {
        tts?.setSpeechRate(speed.speed)
    }

    fun updateTimer(timer: TimerValue) {
        if (timer == TimerValue.OFF) {
            stoppingTime = null
            return
        }

        val now = System.currentTimeMillis()
        stoppingTime = now + TimeUnit.MINUTES.toMillis(timer.value.toLong())
    }
}
