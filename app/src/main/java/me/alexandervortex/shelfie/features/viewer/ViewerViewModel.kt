package me.alexandervortex.shelfie.features.viewer

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.data.repository.BookRepository
import me.alexandervortex.shelfie.features.tts.TtsController
import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.ui.model.ElementUI
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel @Inject constructor(
    private val repo: BookRepository,
    @ApplicationContext private val context: Context, // получаем контекст
) : ViewModel() {

    val error = mutableStateOf("")
    val bookSample: MutableState<BookUI?> = mutableStateOf(null)

    private val ttsController = TtsController(
        context,
        bookSample.value?.titleInfo?.lang?.let { bookLang ->
            Locale(bookLang)
        } ?: Locale.getDefault(),
    ) { errMsg ->
        error.value = errMsg
    }
    val buttonIcon get() = ttsController.buttonIcon

    fun initScreenData(id: String) {
        viewModelScope.launch {
            try {
                bookSample.value = repo.getBookModelById(id)
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

    fun togglePlayPause() {
        val text = bookSample.value?.elements
            ?.filterIsInstance<ElementUI.TextUI>()
            ?.take(5)
            ?.joinToString(" ") { it.text }

        ttsController.togglePlayPause(text)
    }

    // noice
    override fun onCleared() {
        ttsController.release()
        super.onCleared()
    }
}