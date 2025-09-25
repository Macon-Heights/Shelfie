package me.alexandervortex.shelfie.features.viewer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import dagger.hilt.android.lifecycle.HiltViewModel
import me.alexandervortex.shelfie.data.model.BookModel
import javax.inject.Inject

@HiltViewModel
class ViewerPreviewViewModel
@Inject constructor() : BaseViewerViewModel() {

    override val error: MutableState<String> = mutableStateOf("no error")

    override val bookSample: MutableState<BookModel?> = mutableStateOf(
        BookModel(
            id = "",
            localPath = "",
            title = "",
            author = "",
            year = "",
            sections = listOf(LoremIpsum(1000).values.toList())
        )
    )

    override fun initScreenData(id: String) {}
}