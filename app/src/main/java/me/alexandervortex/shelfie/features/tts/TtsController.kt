package me.alexandervortex.shelfie.features.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.mutableStateOf
import me.alexandervortex.shelfie.ui.theme.IC_PAUSE
import me.alexandervortex.shelfie.ui.theme.IC_PLAY
import java.util.Locale

class TtsController(
    context: Context,
    private val bookModel: BookUI?,
    private val onError: (String) -> Unit,
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
                onError.invoke("Язык не поддерживается")
            }
        } else {
            onError("Ошибка инициализации TTS")
        }
    }

    fun togglePlayPause(text: String?) {
        if (text.isNullOrBlank()) {
            onError.invoke("IS NULL OR BLANK ?!\nOMFG CRINGE")
        }

        if (isSpeaking.value) {
            tts?.stop()
            isSpeaking.value = false
            buttonIcon.value = IC_PLAY
        } else {
            tts?.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "utteranceId"
            )
            isSpeaking.value = true
            buttonIcon.value = IC_PAUSE
        }
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
    }
}
