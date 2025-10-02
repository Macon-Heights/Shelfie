package me.alexandervortex.shelfie.features.viewer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.data.repository.BookRepository
import me.alexandervortex.shelfie.ui.model.BookUI
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel
@Inject constructor(
    private val repo: BookRepository,
) : ViewModel() {

    val error = mutableStateOf("no error")
    val bookSample: MutableState<BookUI?> = mutableStateOf(null)

    fun initScreenData(id: String) {
        viewModelScope.launch {
            try {
                bookSample.value = repo.getBookModelById(id) // тут BookUI уже с прогрессом
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
}