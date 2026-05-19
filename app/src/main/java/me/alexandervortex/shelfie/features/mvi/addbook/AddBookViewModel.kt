package me.alexandervortex.shelfie.features.mvi.addbook

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
import me.alexandervortex.shelfie.features.mvi.addbook.mvi.AddBookEffect
import me.alexandervortex.shelfie.features.mvi.addbook.mvi.AddBookIntent
import me.alexandervortex.shelfie.features.mvi.addbook.mvi.AddBookUIState
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel
@Inject constructor(
    private val bookRepository: BookRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AddBookUIState(null))
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AddBookEffect>()
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: AddBookIntent) {
        when (intent) {
            is AddBookIntent.Add -> loadPreview(intent.uri)
            is AddBookIntent.Import -> importBook(intent.uri)
        }
    }

    private fun loadPreview(uri: Uri?) {
        uri?.let {
            viewModelScope.launch {
                try {
                    val titleInfo = bookRepository.previewFromUri(uri)
                    _state.update { state ->
                        state.copy(titleInfo = titleInfo)
                    }
                } catch (e: Exception) {
                    _effect.emit(AddBookEffect.ShowToast("Ошибка: ${e.localizedMessage}"))
                }
            }
        }
    }

    private fun importBook(uri: Uri?) {
        uri?.let {
            viewModelScope.launch {
                try {
                    bookRepository.addBookToDbAndDisk(uri)
                    _effect.emit(AddBookEffect.ShowToast("Книга добавлена"))
                    _effect.emit(AddBookEffect.Close)
                } catch (e: Exception) {
                    _effect.emit(AddBookEffect.ShowToast("Ошибка: ${e.localizedMessage}"))
                }
            }
        }
    }
}