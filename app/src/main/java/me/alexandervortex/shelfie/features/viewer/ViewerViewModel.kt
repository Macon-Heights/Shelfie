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
class ViewerViewModel
@Inject constructor(
    private val repo: BookRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    val bookModel: MutableState<BookUI?> = mutableStateOf(null)

    // needs work
    val error = mutableStateOf("")

    private var ttsController: TtsController? = null

    val buttonIcon get() = ttsController?.buttonIcon

    fun loadCurrentBook(id: String) {
        viewModelScope.launch {
            try {
                val result = repo.getBookModelById(id)
                getNewTts()
                bookModel.value = result
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

    fun togglePlayPause(
        index: Int,
        offset: Int,
    ) {
        val elements = bookModel.value?.elements

        val text = elements
            ?.takeLast(elements.count() - index)
            ?.filterIsInstance<ElementUI.TextUI>()
            ?.take(5)
            ?.joinToString(" ") { it.text }

        ttsController?.togglePlayPause(text)
    }

    override fun onCleared() {
        ttsController?.release()
        super.onCleared()
    }

    private fun getNewTts() {
        ttsController = TtsController(
            context,
            bookModel.value?.titleInfo?.lang?.let { bookLang ->
                Locale(bookLang)
            } ?: Locale.getDefault(),
        ) { errMsg ->
            error.value = errMsg
        }
    }
}