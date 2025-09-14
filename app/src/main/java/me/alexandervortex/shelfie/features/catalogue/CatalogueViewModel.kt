package me.alexandervortex.shelfie.features.catalogue

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.base.ext.toBook
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.data.repository.RepositoryImpl
import javax.inject.Inject

@HiltViewModel
class CatalogueViewModel
@Inject constructor(
    private val repository: RepositoryImpl,
) : ViewModel() {

    val books = mutableStateListOf<BookEntity>()

    fun addBookByUri(uri: Uri, context: Context) {
        viewModelScope.launch {
            val book = uri.toBook(context)
            repository.addBook(book, uri)
            loadBooks()
        }
        // просто добавляю в репо (он сам решит что делать)
        // и обновляю список текущих
    }

    fun loadBooks() {
        viewModelScope.launch {
            books.clear()
            books.addAll(repository.getBooks())
        }
    }

    fun setCurrentBook(item: BookEntity) {
        repository.currentBook = item
    }
}
