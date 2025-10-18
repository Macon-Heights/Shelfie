// features/mediaplayer/TtsController.kt
package me.alexandervortex.shelfie.features.mediaplayer

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.features.viewer.TAG
import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.ui.model.ElementUI
import java.util.Locale

class TtsController(
    context: Context,
    private val bookModel: BookUI?,
    private val onAppError: (String) -> Unit,
    private val scrollToIndex: (index: Int?, partIndex: Int?) -> Unit,
    private val onIconChanged: (Int) -> Unit = {},
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private val isSpeaking = mutableStateOf(false)

    // опционально оставляю, если ты где-то ещё читаешь
    val buttonIcon = mutableStateOf(R.drawable.ic_play)

    override fun onInit(status: Int) {
        Log.d(
            "${TAG}TtsController",
            "onInit:$status"
        )
        val locale = bookModel?.titleInfo?.lang?.let(::Locale) ?: Locale.getDefault()
        if (status == TextToSpeech.SUCCESS) {
            Log.d(
                "${TAG}_TtsController",
                "TextToSpeech.SUCCESS"
            )
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                onAppError("Язык не поддерживается")
                Log.e(
                    "${TAG}_TtsController",
                    "Язык не поддерживается"
                )
            }
            tts?.setOnUtteranceProgressListener(object :
                android.speech.tts.UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    utteranceId?.let {
                        val parts = it.split("_")
                        scrollToIndex(
                            parts.firstOrNull()?.toIntOrNull(),
                            parts.getOrNull(1)?.toIntOrNull()
                        )
                    }
                }

                override fun onDone(utteranceId: String?) {
                    Log.d(
                        "${TAG}_TtsController",
                        "onDone:$utteranceId"
                    )
                }

                override fun onError(utteranceId: String?) {
                    Log.d(
                        "${TAG}_TtsController",
                        "Ошибка TTS: $utteranceId"
                    )
                    onAppError("Ошибка TTS: $utteranceId")
                }
            })
        } else {
            Log.d(
                "${TAG}_TtsController",
                "Ошибка инициализации TTS"
            )
            onAppError("Ошибка инициализации TTS")
        }
    }

    fun togglePlayPause(indexToStartPlaying: Int) {
        Log.d(
            "${TAG}_TtsController",
            "togglePlayPause:${indexToStartPlaying}:${isSpeaking.value}"
        )
        if (isSpeaking.value) stopSpeaking() else startSpeaking(indexToStartPlaying)
    }

    fun release() {
        Log.d(
            "${TAG}_TtsController",
            "release"
        )
        tts?.stop()
        tts?.shutdown()
    }

    private fun startSpeaking(indexToStartPlaying: Int) {
        if (bookModel?.elements.isNullOrEmpty()) {
            onAppError("BookModel elements are null or empty")
            return
        }
        tts?.stop()
        isSpeaking.value = true
        buttonIcon.value = R.drawable.ic_pause
        onIconChanged(R.drawable.ic_pause)

        bookModel?.elements?.forEachIndexed { elementIndex, element ->
            val textUi = element as? ElementUI.TextUI ?: return@forEachIndexed
            textUi.parts.orEmpty().forEachIndexed { sentenceIndex, sentence ->
                val isIndexNeeded = elementIndex >= indexToStartPlaying
                val trimmed = sentence?.trim().orEmpty()
                if (isIndexNeeded && trimmed.isNotBlank()) {
                    tts?.speak(
                        trimmed,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        "${elementIndex}_${sentenceIndex}"
                    )
                }
            }
        }
    }

    private fun stopSpeaking() {
        Log.d(
            "${TAG}_TtsController",
            "stopSpeaking"
        )
        tts?.stop()
        isSpeaking.value = false
        buttonIcon.value = R.drawable.ic_play
        onIconChanged(R.drawable.ic_play)
    }
}
