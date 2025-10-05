package me.alexandervortex.shelfie.features.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.mutableStateOf
import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.theme.IC_PAUSE
import me.alexandervortex.shelfie.ui.theme.IC_PLAY
import java.util.Locale

class TtsController(
    context: Context,
    private val bookModel: BookUI?,
    private val onAppError: (String) -> Unit,
    private val scrollToIndex: (Int) -> Unit, // ✅ новый колбэк
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private val isSpeaking = mutableStateOf(false)

    val buttonIcon = mutableStateOf(IC_PLAY)

    override fun onInit(status: Int) {
        tts?.setVoice(tts?.voices?.random())
        val locale = bookModel?.titleInfo
            ?.lang
            ?.let { bookLang ->
                Locale(bookLang)
            } ?: Locale.getDefault()

        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                onAppError("Язык не поддерживается")
            }

            tts?.setOnUtteranceProgressListener(
                object : android.speech.tts.UtteranceProgressListener() {

                    override fun onStart(utteranceId: String?) {
                        utteranceId?.toIntOrNull()?.let { scrollToIndex(it) }
                    }

                    override fun onDone(utteranceId: String?) {}

                    override fun onError(utteranceId: String?) {
                        onAppError("Ошибка TTS utterance ${utteranceId.toString()}")
                    }
                }
            )
        } else { onAppError("Ошибка инициализации TTS") }
    }

    fun togglePlayPause(indexToStartPlaying: Int) {
        if (isSpeaking.value) {
            stopSpeaking()
        } else {
            startSpeaking(indexToStartPlaying)
        }
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
    }

    fun startSpeaking(indexToStartPlaying: Int) {
        if (bookModel?.elements.isNullOrEmpty()) {
            onAppError("BookModel elements are null or empty")
            return
        }

        tts?.stop()
        bookModel?.elements?.forEachIndexed { elementIndex, elementUI ->
            if (
                canISpeak(
                    elementUI = elementUI,
                    indexToStartPlaying = indexToStartPlaying,
                    elementIndex = elementIndex
                )
            ) {
                tts?.speak(
                    (elementUI as ElementUI.TextUI).text.trim(),
                    TextToSpeech.QUEUE_ADD,
                    null,
                    elementIndex.toString()
                )
            }
        }
        isSpeaking.value = true
        buttonIcon.value = IC_PAUSE
    }

    fun stopSpeaking() {
        tts?.stop()
        isSpeaking.value = false
        buttonIcon.value = IC_PLAY
    }

    private fun canISpeak(
        elementUI: ElementUI,
        indexToStartPlaying: Int,
        elementIndex: Int,
    ): Boolean {
        val isTextUI = elementUI is ElementUI.TextUI
        val isCurrentIndex = elementIndex >= indexToStartPlaying
        val isNotEmpty = (elementUI as? ElementUI.TextUI)?.text?.trim()?.isNotBlank() == true
        return isTextUI && isCurrentIndex && isNotEmpty
    }
}
