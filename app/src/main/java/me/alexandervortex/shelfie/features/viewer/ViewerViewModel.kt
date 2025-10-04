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

    val isScrollable = mutableStateOf(true)
    val error = mutableStateOf("")
    val bookModel: MutableState<BookUI?> = mutableStateOf(null)

    private var ttsController: TtsController? = null
    val buttonIcon get() = ttsController?.buttonIcon

    fun loadCurrentBook(id: String) {
        viewModelScope.launch {
            try {
                val result = repo.getBookModelById(id)
                bookModel.value = result
                createTTSWithLocale()
            } catch (e: Exception) {
                error.value = e.localizedMessage ?: "unknown viewmodel error"
            }
        }
    }

    private fun createTTSWithLocale() {
        ttsController = TtsController(
            context = context,
            bookModel = bookModel.value,
            onError = { error -> this.error.value = error },
    }

    fun saveScrollStateOnDispose(
        id: String,
        index: Int,
        offset: Int,
    ) {
        viewModelScope.launch {
            repo.saveCurrentBookProgress(id, index, offset)
        }
    }

    fun togglePlayPause(
        index: Int,
        offset: Int,
    ) {
        val elements = bookModel.value?.elements

        val text = elements
            ?.subList(index, elements.lastIndex)
            ?.filterIsInstance<ElementUI.TextUI>()
            ?.filter { it.text.isNotBlank() }
            ?.take(5)
            ?.joinToString(" ") { it.text }

        ttsController?.togglePlayPause(text)
    }

    override fun onCleared() {
        ttsController?.release()
        super.onCleared()
    }
}