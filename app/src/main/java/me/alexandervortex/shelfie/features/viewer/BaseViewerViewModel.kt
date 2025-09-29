package me.alexandervortex.shelfie.features.viewer

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import me.alexandervortex.shelfie.data.model.BookUi

abstract class BaseViewerViewModel : ViewModel() {

    abstract val error: MutableState<String>
    abstract val bookSample: MutableState<BookUi?>

    abstract fun initScreenData(id: String)
}
