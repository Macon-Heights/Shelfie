package me.alexandervortex.shelfie.feature.preview

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
import me.alexandervortex.shelfie.data.repository.CatalogueRepository
import me.alexandervortex.shelfie.model.PreviewBookModel
import javax.inject.Inject

@HiltViewModel
class PreviewScreenViewModel
@Inject constructor(
    private val catalogueRepository: CatalogueRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PreviewBookModel())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<PreviewScreenEffect>()
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: PreviewScreenIntent) {
        when (intent) {
            is PreviewScreenIntent.Add -> loadPreview(intent.uri)
            is PreviewScreenIntent.Import -> importBook(intent.uri)
        }
    }

    private fun loadPreview(uri: Uri?) {
        uri?.let {
            viewModelScope.launch {
                try {
                    val titleInfo = catalogueRepository.previewBook(uri)
                    _state.update { titleInfo ?: it }
                } catch (e: Exception) {
                    _effect.emit(PreviewScreenEffect.ShowToast("Error: ${e.localizedMessage}"))
                }
            }
        }
    }

    private fun importBook(uri: Uri?) {
        uri?.let {
            viewModelScope.launch {
                try {
                    catalogueRepository.addBook(uri)
                    _effect.emit(PreviewScreenEffect.ShowToast("Book added"))
                    _effect.emit(PreviewScreenEffect.Close)
                } catch (e: Exception) {
                    _effect.emit(PreviewScreenEffect.ShowToast("Error: ${e.localizedMessage}"))
                }
            }
        }
    }
}