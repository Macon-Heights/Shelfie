package me.alexandervortex.shelfie.features.catalogue

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.alexandervortex.shelfie.base.BaseCatalogueViewModel
import me.alexandervortex.shelfie.data.db.entiry.BookUri
import me.alexandervortex.shelfie.data.repository.RepositoryImpl
import javax.inject.Inject

@HiltViewModel
class CatalogueViewModel
@Inject constructor(
    private val repository: RepositoryImpl,
) : BaseCatalogueViewModel() {

    override val books = mutableStateListOf<BookUri>()

    override fun addBookByUri(
        uri: Uri,
        context: Context,
    ) {
        viewModelScope.launch {
            repository.addBook(context, uri)
            loadBooks()
        }
        // просто добавляю в репо (он сам решит что делать)
        // и обновляю список текущих
    }

    override fun loadBooks() {
        viewModelScope.launch {
            books.clear()
            books.addAll(repository.getBooks())
        }
    }

    override fun setCurrentBook(item: BookUri) {
        repository.saveTheBook(item)
    }
}
