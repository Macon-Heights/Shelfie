package me.alexandervortex.shelfie.features.viewer

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import me.alexandervortex.shelfie.ui.model.BookUI

abstract class BaseViewerViewModel : ViewModel() {

    abstract val error: MutableState<String>
    abstract val bookSample: MutableState<BookUI?>

    abstract fun initScreenData(id: String)
}
