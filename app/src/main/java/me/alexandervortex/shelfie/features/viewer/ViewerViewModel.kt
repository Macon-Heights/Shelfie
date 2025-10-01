package me.alexandervortex.shelfie.features.viewer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.data.repository.BookRepository
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel
@Inject constructor(
    private val repo: BookRepository,
) : BaseViewerViewModel() {

    override val error = mutableStateOf("no error")
    override val bookSample: MutableState<BookUI?> = mutableStateOf(null)

    override fun initScreenData(id: String) {
        viewModelScope.launch {
            try {
                bookSample.value = repo.getBookModelById(id)
                error.value = "no error"
            } catch (e: Exception) {
                error.value = e.localizedMessage ?: "unknown error"
            }
        }
    }
}