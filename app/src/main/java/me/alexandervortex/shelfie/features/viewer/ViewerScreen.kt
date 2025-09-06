package me.alexandervortex.shelfie.features.viewer

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.kursx.parser.fb2.Element
import com.kursx.parser.fb2.Section

@Composable
fun ViewerScreen(viewModel: ViewerViewModel) {
    val context = LocalContext.current
    LaunchedEffect(true) { viewModel.initScreenData(context) }

    val items: List<String> = viewModel.bookSample.value?.let { book ->
        book.body.sections.flatMap { section: Section ->
            section.elements.map { element: Element? ->
                element?.text
            }
        }
    }?.filterNotNull().orEmpty()

    LazyColumn {
        items(items) { name: String ->
            Text(text = name)
        }
    }
}