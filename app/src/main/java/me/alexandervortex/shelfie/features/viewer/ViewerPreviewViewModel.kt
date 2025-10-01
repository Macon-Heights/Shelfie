package me.alexandervortex.shelfie.features.viewer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import me.alexandervortex.shelfie.ui.model.BookUi
import me.alexandervortex.shelfie.ui.model.SectionUi
import javax.inject.Inject

@HiltViewModel
class ViewerPreviewViewModel
@Inject constructor() : BaseViewerViewModel() {

    override val error: MutableState<String> = mutableStateOf("no error")

    override val bookSample: MutableState<BookUi?> = mutableStateOf(
        // todo
        BookUi(
            id = "",
            localPath = "",
            title = "",
            author = "",
            year = "",
            sections = listOf(
                SectionUi(
                    title = "",
                    blocks = listOf(),
                    subsections = listOf()
                )
            ),
            coverImage = null,
            annotation = null,
            genre = null
        )
    )

    override fun initScreenData(id: String) {}
}