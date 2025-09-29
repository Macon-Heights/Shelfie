package me.alexandervortex.shelfie.features.catalogue

import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import me.alexandervortex.shelfie.data.db.entiry.BookEntity

abstract class BaseCatalogueViewModel : ViewModel() {

    abstract val books: SnapshotStateList<BookEntity>

    abstract fun importFromUri(uri: Uri)

    abstract fun getBookEntities()
}