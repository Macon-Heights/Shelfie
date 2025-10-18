package me.alexandervortex.shelfie.features._deprecated_viewer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
) : BaseViewerViewModel() {

    override val errorState = mutableStateOf("")
    override val bookModel: MutableState<BookUI?> = mutableStateOf(null)

    override fun loadCurrentBook(id: String) {
        viewModelScope.launch {
            try {
                bookModel.value = repo.getBookModelById(id) // with offset
            } catch (e: Exception) {
                errorState.value = e.localizedMessage ?: "unknown viewmodel error"
            }
        }
    }

    override fun saveScrollStateOnDispose(
        id: String,
        index: Int,
        offset: Int,
    ) {
        viewModelScope.launch {
            repo.saveCurrentBookProgress(id, index, offset)
        }
    }
}