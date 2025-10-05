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
    private val scrollToIndex: (index: Int?, partIndex: Int?) -> Unit,
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
                        onAppError("-- onStart ${utteranceId.toString()}")
                        utteranceId?.let { id ->
                            val indexOrNull = id.split("_").firstOrNull()
                            val partIndexOrNull = id.split("_").getOrNull(1)
                            scrollToIndex(
                                indexOrNull?.toIntOrNull(),
                                partIndexOrNull?.toIntOrNull()
                            )
                        }
                    }

                    override fun onDone(utteranceId: String?) {
                        onAppError("-- onDone ${utteranceId.toString()}")
                    }

                    override fun onError(utteranceId: String?) {
                        onAppError("Ошибка TTS utterance ${utteranceId.toString()}")
                    }
                }
            )
        } else {
            onAppError("Ошибка инициализации TTS")
        }
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

    private fun startSpeaking(indexToStartPlaying: Int) {
        if (bookModel?.elements.isNullOrEmpty()) {
            onAppError("BookModel elements are null or empty")
            return
        }

        tts?.stop()
        bookModel?.elements?.forEachIndexed { elementIndex, element ->
            val currentSentences = (element as? ElementUI.TextUI)?.parts.orEmpty()

            val isIndexNeeded = elementIndex >= indexToStartPlaying
            val isTextUI = element is ElementUI.TextUI

            if (isIndexNeeded && isTextUI) {
                currentSentences.forEachIndexed { sentenceIndex, sentence ->
                    val isNotEmpty = currentSentences.getOrNull(sentenceIndex)?.trim()
                        ?.isNotBlank() == true

                    if (isNotEmpty) {
                        tts?.speak(
                            sentence.trim(),
                            TextToSpeech.QUEUE_ADD,
                            null,
                            "${elementIndex}_${sentenceIndex}"
                        )
                        isSpeaking.value = true
                        buttonIcon.value = IC_PAUSE
                    }
                }
            }
        }
    }

    private fun stopSpeaking() {
        tts?.stop()
        isSpeaking.value = false
        buttonIcon.value = IC_PLAY
    }
}
