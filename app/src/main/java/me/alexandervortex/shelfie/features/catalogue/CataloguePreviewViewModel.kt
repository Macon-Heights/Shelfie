package me.alexandervortex.shelfie.features.catalogue

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import dagger.hilt.android.lifecycle.HiltViewModel
import me.alexandervortex.shelfie.base.BaseCatalogueViewModel
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import javax.inject.Inject

@HiltViewModel
class CataloguePreviewViewModel
@Inject constructor() : BaseCatalogueViewModel() {

    override val books = mutableStateListOf<BookEntity>()

    override fun addBookByUri(uri: Uri, context: Context) {
        
    }

    override fun loadBooks() {

    }

    override fun setCurrentBook(item: BookEntity) {

    }
}
