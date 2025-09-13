package me.alexandervortex.shelfie.features.filepicker

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.data.repository.BookRepo
import javax.inject.Inject

@HiltViewModel
class FilePickerViewModel
@Inject constructor(
    private val repo: BookRepo,
) : ViewModel() {

    val books = mutableStateListOf<BookEntity>()

    fun onBookAdded(uri: Uri) {
        viewModelScope.launch {
            val book = BookEntity(
                uid = uri.hashCode(),
                uri = uri.toString(),
                name = uri.lastPathSegment ?: "noname"
            )
            repo.insert(book)
            loadBooks()
        }
    }

    fun loadBooks() {
        viewModelScope.launch {
            val result = repo.getAll()
            books.clear()
            books.addAll(result.toMutableStateList())
        }
    }

    fun setCurrentBook(item: BookEntity) {
        repo.currentBook = item
    }
}