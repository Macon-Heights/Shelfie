package me.alexandervortex.shelfie.features.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.mutableStateOf
import me.alexandervortex.shelfie.ui.theme.IC_PAUSE
import me.alexandervortex.shelfie.ui.theme.IC_PLAY
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TtsController
@Inject constructor(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private val isSpeaking = mutableStateOf(false)

    val buttonIcon = mutableStateOf(IC_PLAY)
    val errorMessage = mutableStateOf("")

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.getDefault())
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                errorMessage.value = "Язык не поддерживается"
            }
        } else {
            errorMessage.value = "Ошибка инициализации TTS"
        }
    }

    fun togglePlayPause(text: String?) {
        if (text.isNullOrBlank()) return

        if (isSpeaking.value) {
            stop()
        } else {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
            isSpeaking.value = true
            buttonIcon.value = IC_PAUSE
        }
    }

    fun stop() {
        tts?.stop()
        isSpeaking.value = false
        buttonIcon.value = IC_PLAY
    }

    fun setLanguage(langCode: String) {
        val locale = Locale(langCode)
        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            errorMessage.value = "Язык $langCode не поддерживается"
        }
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
    }
}
