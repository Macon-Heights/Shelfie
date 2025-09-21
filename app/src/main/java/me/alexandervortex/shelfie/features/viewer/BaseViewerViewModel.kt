package me.alexandervortex.shelfie.features.viewer

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import me.alexandervortex.shelfie.data.model.BookModel

abstract class BaseViewerViewModel : ViewModel() {

    abstract val error: MutableState<String>
    abstract val bookSample: MutableState<BookModel?>

    abstract fun initScreenData(context: Context)
}
