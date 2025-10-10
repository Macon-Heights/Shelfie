package me.alexandervortex.shelfie.features.catalogue

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.data.repository.BookRepository
import me.alexandervortex.shelfie.features.catalogue.ui.model.CatalogueBooksState
import me.alexandervortex.shelfie.features.catalogue.ui.model.CatalogueLoadingState
import me.alexandervortex.shelfie.features.catalogue.ui.model.UIState
import javax.inject.Inject

@HiltViewModel
class CatalogueViewModel
@Inject constructor(
    private val bookRepository: BookRepository,
) : ViewModel() {

    val uiState: MutableState<UIState> = mutableStateOf(CatalogueLoadingState)

    fun importFromUri(uri: Uri) {
        viewModelScope.launch {
            bookRepository.importFromUri(uri)
            getBookEntities()
        }
    }

    fun getBookEntities() {
        viewModelScope.launch {
            val newEntities = bookRepository.getBookEntities()
            val state = CatalogueBooksState(newEntities)
            uiState.value = state
        }
    }
}
