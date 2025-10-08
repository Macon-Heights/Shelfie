package me.alexandervortex.shelfie.features.catalogue.base

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import me.alexandervortex.shelfie.features.catalogue.model.CatalogueUiState

abstract class BaseCatalogueViewModel : ViewModel() {

    abstract val uiState: MutableState<CatalogueUiState>

    abstract fun importFromUri(uri: Uri)

    abstract fun getBookEntities()
}