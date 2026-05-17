package me.alexandervortex.shelfie.features.addbook

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import me.alexandervortex.shelfie.features.addbook.mvi.AddBookIntent
import me.alexandervortex.shelfie.features.addbook.mvi.AddBookState
import me.alexandervortex.shelfie.features.mvi.catalogue.mvi.CatalogueEffect
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel
@Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(AddBookState("loading info"))
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CatalogueEffect>()
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: AddBookIntent) {
        when (intent) {
            is AddBookIntent.Add -> showPreview(intent.uri)
        }
    }

    private fun showPreview(uri: Uri) {
        // todo: add logic
    }
}