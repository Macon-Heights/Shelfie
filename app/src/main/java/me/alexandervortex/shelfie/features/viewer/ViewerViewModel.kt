package me.alexandervortex.shelfie.features.viewer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.data.repository.BookRepository
import me.alexandervortex.shelfie.features.tts.TtsController
import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.ui.model.ElementUI
import javax.inject.Inject
@HiltViewModel
class ViewerViewModel @Inject constructor(
    private val repo: BookRepository,
    private val ttsController: TtsController
) : ViewModel() {

    val error = ttsController.errorMessage
    val bookSample = mutableStateOf<BookUI?>(null)
    val buttonIcon get() = ttsController.buttonIcon

    fun initScreenData(id: String) {
        viewModelScope.launch {
            try {
                bookSample.value = repo.getBookModelById(id)
                bookSample.value?.titleInfo?.lang?.let { ttsController.setLanguage(it) }
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

    override fun onCleared() {
        ttsController.release()
        super.onCleared()
    }
}