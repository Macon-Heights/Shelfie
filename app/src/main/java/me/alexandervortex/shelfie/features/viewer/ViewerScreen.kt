package me.alexandervortex.shelfie.features.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
//        val sections: List<ElementUI> = viewModel.bookSample.value?.sections.orEmpty()
//        items(sections) { section ->
//            section.blocks.forEachIndexed { i, element ->
//                BlockComponent(element)
//            }
//        }
    }

}