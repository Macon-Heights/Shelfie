package me.alexandervortex.shelfie.features.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.alexandervortex.shelfie.ui.model.SectionUi

@Composable
fun ViewerScreen(
    viewModel: BaseViewerViewModel,
    id: String,
) {
    LaunchedEffect(true) { viewModel.initScreenData(id) }
    LazyColumn(
        contentPadding = PaddingValues(32.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // todo
        val sections: List<SectionUi> = viewModel.bookSample.value?.sections.orEmpty()

        items(sections) { section ->
            section.blocks.forEachIndexed { i, element ->
                Text(
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.background(randomColor(i)),
                    text = element.toString(),
                )
            }
            Spacer(
                Modifier
                    .size(height = 24.dp, width = 240.dp)
                    .background(Color.Red)
            )
        }
    }

}

fun getColors(): List<Color> {
    return listOf(
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Magenta,
        Color.Cyan,
        Color.Gray,
    )
}

inline fun randomColor(i: Int): Color {
    val index = i % getColors().size
    return getColors()[index]
}

@Composable
@Preview
fun ViewerScreen() {
    ViewerScreen(hiltViewModel<ViewerPreviewViewModel>(), "id")
}