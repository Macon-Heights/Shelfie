package me.alexandervortex.shelfie.features.catalogue.preview

import android.net.Uri
import androidx.compose.runtime.toMutableStateList
import dagger.hilt.android.lifecycle.HiltViewModel
import me.alexandervortex.shelfie.features.catalogue.base.BaseCatalogueViewModel
import me.alexandervortex.shelfie.features.catalogue.preview.BookPreviewFactory.getBooks
import javax.inject.Inject

@HiltViewModel
class CataloguePreviewViewModel
@Inject constructor() : BaseCatalogueViewModel() {

    override val books = getBooks().toMutableStateList()

    override fun importFromUri(uri: Uri) {}

    override fun getBookEntities() {}
}
