package me.alexandervortex.shelfie.features.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ViewerScreen(
    viewModel: BaseViewerViewModel,
) {
    val context = LocalContext.current
    LaunchedEffect(true) { viewModel.initScreenData(context) }

    LazyColumn(Modifier.background(MaterialTheme.colorScheme.background)) {
        items(viewModel.bookSample.value?.list.orEmpty()) { line ->
            Text(
                color = MaterialTheme.colorScheme.onBackground,
                text = line.orEmpty()
            )
        }
    }
}

@Composable
@Preview
fun ViewerScreen() {
    ViewerScreen(hiltViewModel<ViewerPreviewViewModel>())
}