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
class ViewerBookViewModel
@Inject constructor(
    private val repo: BookRepository,
) : ViewModel() {

    val errorState = mutableStateOf("")
    val bookModel: MutableState<BookUI?> = mutableStateOf(null)

    fun loadCurrentBook(id: String) {
        viewModelScope.launch {
            try {
                bookModel.value = repo.getBookModelById(id) // with offset
            } catch (e: Exception) {
                errorState.value = e.localizedMessage ?: "unknown viewmodel error"
            }
        }
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
}