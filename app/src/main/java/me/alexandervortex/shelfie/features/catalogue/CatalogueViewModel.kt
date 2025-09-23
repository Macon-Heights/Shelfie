package me.alexandervortex.shelfie.features.catalogue

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.base.BaseCatalogueViewModel
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.data.repository.Repository
import javax.inject.Inject

@HiltViewModel
class CatalogueViewModel
@Inject constructor(
    private val repository: Repository,
) : BaseCatalogueViewModel() {

    override val books = mutableStateListOf<BookEntity>()

    override fun addBookByUri(
        uri: Uri,
        context: Context,
    ) {
        viewModelScope.launch {
            repository.addBookByUri(uri)
            loadBooks()
        }
    }

    override fun loadBooks() {
        viewModelScope.launch {
            books.clear()
            books.addAll(repository.getBookEntities())
        }
    }
}
