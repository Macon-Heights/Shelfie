package me.alexandervortex.shelfie.features.viewer

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import me.alexandervortex.shelfie.features.tts.TtsController
import me.alexandervortex.shelfie.ui.model.BookUI
import javax.inject.Inject

@HiltViewModel
class TtsViewModel
@Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    val isScrollable = mutableStateOf(true)
    val errorState = mutableStateOf("")

    val scrollElementIndex = mutableStateOf(0)
    val scrollElementPart = mutableStateOf(0)

    private var ttsController: TtsController? = null
    val buttonIcon get() = ttsController?.buttonIcon // FIXME check taht

    fun initTTSWithBook(bookUI: BookUI) {
        scrollElementIndex.value = bookUI.progressIndex
        ttsController = TtsController(
            context = context,
            bookModel = bookUI,
            onAppError = { error ->
                this.errorState.value = error
            },
            scrollToIndex = { index, partIndex ->
                scrollElementIndex.value = index ?: 0
                scrollElementPart.value = partIndex ?: 0
            }
        )
    }

    fun togglePlayPause(
        indexToStartPlaying: Int,
    ) {
        ttsController?.togglePlayPause(indexToStartPlaying)
    }

    override fun onCleared() {
        ttsController?.release()
        super.onCleared()
    }
}