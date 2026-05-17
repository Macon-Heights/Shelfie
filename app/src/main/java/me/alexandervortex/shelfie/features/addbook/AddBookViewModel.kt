package me.alexandervortex.shelfie.features.addbook

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
import me.alexandervortex.shelfie.features.addbook.mvi.AddBookEffect
import me.alexandervortex.shelfie.features.addbook.mvi.AddBookIntent
import me.alexandervortex.shelfie.features.addbook.mvi.AddBookState
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel
@Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(AddBookState("loading info"))
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AddBookEffect>()
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: AddBookIntent) {
        when (intent) {
            is AddBookIntent.Add -> loadPreview(intent.uri)
        }
    }

    private fun loadPreview(uri: Uri?) {
        viewModelScope.launch {
            uri?.let {
                _state.update { state ->
                    state.copy(name = uri.toString())
                }
            }
        }
    }
}