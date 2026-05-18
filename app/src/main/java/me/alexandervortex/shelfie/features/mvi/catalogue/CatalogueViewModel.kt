package me.alexandervortex.shelfie.features.mvi.catalogue

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.data.repository.BookRepository
import me.alexandervortex.shelfie.features.mvi.catalogue.mvi.CatalogueEffect
import me.alexandervortex.shelfie.features.mvi.catalogue.mvi.CatalogueIntent
import me.alexandervortex.shelfie.features.mvi.catalogue.mvi.CatalogueState
import me.alexandervortex.shelfie.ui.model.CatalogueItemUIModel
import javax.inject.Inject

@HiltViewModel
class CatalogueViewModel @Inject constructor(
    private val bookRepository: BookRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogueState(books = getSkeletons()))
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CatalogueEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            bookRepository.getPreviews().collect { dbBooks ->
                _state.update { current ->
                    // Если книг нет, можем оставить скелетоны или пустой список. 
                    // Но обычно Flow вернет пустой список если в БД пусто.
                    val updatedBooks = dbBooks.map { dbBook ->
                        val currentBook = current.books.filterIsInstance<CatalogueItemUIModel.Model>()
                            .find { it.id == dbBook.id }
                        dbBook.copy(isChecked = currentBook?.isChecked ?: false)
                    }
                    current.copy(books = updatedBooks)
                }
            }
        }
    }

    fun onIntent(intent: CatalogueIntent) {
        when (intent) {
            is CatalogueIntent.LoadBooks -> { /* flow starts in init */ }
            is CatalogueIntent.ImportBook -> importBook(intent.uri)
            is CatalogueIntent.ToggleRemoveMode -> toggleRemoveMode(intent.id)
            is CatalogueIntent.ToggleBookCheck -> toggleBookCheck(intent.id)
            is CatalogueIntent.RemoveChecked -> removeChecked()
            is CatalogueIntent.TogglePopup -> togglePopup(intent.isEnabled)
        }
    }

    private fun togglePopup(enabled: Boolean) {
        _state.update {
            it.copy(isPopup = enabled)
        }
    }

    private fun getSkeletons(): List<CatalogueItemUIModel> {
        return (1..9).map { index ->
            CatalogueItemUIModel.Skeleton
        }
    }

    private fun importBook(uri: Uri) = viewModelScope.launch {
        try {
            bookRepository.importFromUri(uri)
            _effect.emit(CatalogueEffect.ShowToast("Книга добавлена"))
        } catch (e: Exception) {
            _effect.emit(CatalogueEffect.ShowToast("Ошибка: ${e.localizedMessage}"))
        }
    }

    private fun toggleRemoveMode(id: String) {
        _state.update { current ->
            val newMode = !current.isRemoveMode
            val updatedBooks =
                current.books.filterIsInstance<CatalogueItemUIModel.Model>().map { book ->
                    book.copy(isChecked = if (book.id == id) !book.isChecked else false)
                }
            current.copy(isRemoveMode = newMode, books = updatedBooks)
        }
    }

    private fun toggleBookCheck(id: String) {
        _state.update { current ->
            val updatedBooks =
                current.books.filterIsInstance<CatalogueItemUIModel.Model>().map { book ->
                    if (book.id == id) book.copy(isChecked = !book.isChecked) else book
                }
            current.copy(books = updatedBooks)
        }
    }

    private fun removeChecked() = viewModelScope.launch {
        val checkedBooks =
            _state.value.books.filterIsInstance<CatalogueItemUIModel.Model>()
                .filter { it.isChecked }
        if (checkedBooks.isEmpty()) {
            _effect.emit(CatalogueEffect.ShowToast("Ничего не выбрано"))
            return@launch
        }

        bookRepository.removeBooks(checkedBooks)
        _effect.emit(CatalogueEffect.ShowToast("Удалено ${checkedBooks.size} книг"))
        _state.update { it.copy(isRemoveMode = false) }
    }
}