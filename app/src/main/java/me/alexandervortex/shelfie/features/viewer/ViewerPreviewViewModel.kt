package me.alexandervortex.shelfie.features.viewer

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.alexandervortex.shelfie.data.model.BookModel
import javax.inject.Inject

@HiltViewModel
class ViewerPreviewViewModel
@Inject constructor() : BaseViewerViewModel() {

    override val error: MutableState<String> = mutableStateOf("no error")

    override val bookSample: MutableState<BookModel?> = mutableStateOf(
        BookModel(
            LoremIpsum(1000).values.toList()
        )
    )

    override fun initScreenData(context: Context) {}
}

@Composable
@Preview
fun ViSewerScreen() {
    ViewerScreen(hiltViewModel<ViewerPreviewViewModel>())
}