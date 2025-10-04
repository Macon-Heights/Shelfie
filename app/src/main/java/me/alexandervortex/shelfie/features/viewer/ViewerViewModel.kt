package me.alexandervortex.shelfie.features.viewer

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.data.repository.BookRepository
import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.ui.model.ElementUI
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel
@Inject constructor(
    private val repo: BookRepository,
    application: Application,
) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    val error = mutableStateOf("no error")
    val bookSample: MutableState<BookUI?> = mutableStateOf(null)
    val buttonIcon: MutableState<ImageVector> = mutableStateOf(Icons.Default.PlayArrow)

    private var tts: TextToSpeech? = null
    private var isSpeaking = mutableStateOf(false)

    init {
        tts = TextToSpeech(application, this)
    }

    fun initScreenData(id: String) {
        viewModelScope.launch {
            try {
                bookSample.value = repo.getBookModelById(id)
                error.value = "no error"
            } catch (e: Exception) {
                error.value = e.localizedMessage ?: "unknown error"
            }
        }
    }

    fun saveProgress(bookId: String, index: Int, offset: Int) {
        viewModelScope.launch {
            repo.updateBookProgress(bookId, index, offset)
        }
    }

    override fun onCleared() {
        tts?.stop()
        tts?.shutdown()
        super.onCleared()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("ru", "RU"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                error.value = "Язык не поддерживается"
            }
        } else {
            error.value = "Ошибка инициализации TTS"
        }
    }

    fun togglePlayPause() {
        val text = bookSample.value?.elements
            ?.filterIsInstance<ElementUI.TextUI>()
            ?.take(5) // только первые 5 абзацев
            ?.joinToString(" ") { it.text }
            ?: return

        if (isSpeaking.value) {
            tts?.stop()
            isSpeaking.value = false
            buttonIcon.value = Icons.Rounded.PlayArrow
        } else {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
            isSpeaking.value = true
            buttonIcon.value = Icons.Rounded.Pause
        }
    }
}