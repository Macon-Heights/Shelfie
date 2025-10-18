package me.alexandervortex.shelfie.features._deprecated_viewer

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import me.alexandervortex.shelfie.ui.model.BookUI

abstract class BaseViewerViewModel : ViewModel() {

    abstract val errorState: MutableState<String>
    abstract val bookModel: MutableState<BookUI?>

    abstract fun loadCurrentBook(id: String)
    abstract fun saveScrollStateOnDispose(
        id: String,
        index: Int,
        offset: Int,
    )
}
