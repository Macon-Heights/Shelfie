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
import me.alexandervortex.shelfie.data.model.SectionModel

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
        val sections: List<SectionModel> = viewModel.bookSample.value?.sections.orEmpty()

        items(sections) { section ->
            section.elements.forEach { element ->
                Text(
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.background(randomColor()),
                    text = element
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

inline fun randomColor(): Color {
    return listOf(
        Color.Red,
        Color.Green,
        Color.Blue,

        Color.Yellow,
        Color.Magenta,
        Color.Cyan,
        Color.Gray,
    ).random()
}

@Composable
@Preview
fun ViewerScreen() {
    ViewerScreen(hiltViewModel<ViewerPreviewViewModel>(), "id")
}