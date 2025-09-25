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
        val sections: List<List<String>> = viewModel.bookSample.value?.sections.orEmpty()

        items(sections) { section ->
            Text(
                textAlign = TextAlign.Justify,
                color = MaterialTheme.colorScheme.onBackground,
                text = section.joinToString("\n\n\n---===---\n\n\n")
            )
            Spacer(Modifier
                .size(128.dp)
                .background(Color.Red))
        }
    }
}

@Composable
@Preview
fun ViewerScreen() {
    ViewerScreen(hiltViewModel<ViewerPreviewViewModel>(), "id")
}