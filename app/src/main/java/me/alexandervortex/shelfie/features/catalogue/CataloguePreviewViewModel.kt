package me.alexandervortex.shelfie.features.catalogue

import android.net.Uri
import androidx.compose.runtime.toMutableStateList
import dagger.hilt.android.lifecycle.HiltViewModel
import me.alexandervortex.shelfie.base.BaseCatalogueViewModel
import me.alexandervortex.shelfie.features.catalogue.BookPreviewFactory.getBooks
import javax.inject.Inject

@HiltViewModel
class CataloguePreviewViewModel
@Inject constructor() : BaseCatalogueViewModel() {

    override val books = getBooks().toMutableStateList()

    override fun importFromUri(uri: Uri) {}

    override fun getBookEntities() {}
}
