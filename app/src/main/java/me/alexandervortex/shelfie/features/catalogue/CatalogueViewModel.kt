package me.alexandervortex.shelfie.features.catalogue

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.data.repository.BookRepository
import me.alexandervortex.shelfie.features.catalogue.ui.model.UIState
import javax.inject.Inject

@HiltViewModel
class CatalogueViewModel
@Inject constructor(
    private val bookRepository: BookRepository,
) : ViewModel() {

    val uiState: MutableState<UIState> = mutableStateOf(UIState.CatalogueLoadingState)
    val error: MutableState<String?> = mutableStateOf(null)

    fun importFromUri(uri: Uri) {
        viewModelScope.launch {
            try {
                bookRepository.importFromUri(uri)
            } catch (e: Exception) {
                error.value = e.localizedMessage
            }
            getBookEntities()
        }
    }

    fun getBookEntities() {
        viewModelScope.launch {
            val newEntities = bookRepository.getBookEntities()
            val state = UIState.CatalogueBooksState(newEntities)
            uiState.value = state
        }
    }
}
