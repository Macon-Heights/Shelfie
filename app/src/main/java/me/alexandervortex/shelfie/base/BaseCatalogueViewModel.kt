package me.alexandervortex.shelfie.base

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import me.alexandervortex.shelfie.data.db.entiry.BookUri

abstract class BaseCatalogueViewModel : ViewModel() {

    abstract val books: SnapshotStateList<BookUri>

    abstract fun addBookByUri(uri: Uri, context: Context)

    abstract fun loadBooks()

    abstract fun setCurrentBook(item: BookUri)
}