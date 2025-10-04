package me.alexandervortex.shelfie.features.viewer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import me.alexandervortex.shelfie.ui.component.ComponentUI
import me.alexandervortex.shelfie.ui.model.ElementUI

@Composable
fun ViewerScreen(
    viewModel: ViewerViewModel,
    id: String,
) {
    val listState = rememberLazyListState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val book = viewModel.bookSample.value

    // загружаем книгу
    LaunchedEffect(true) { viewModel.initScreenData(id) }

    // после загрузки книги → восстанавливаем позицию
    LaunchedEffect(book?.progressIndex, book?.progressOffset) {
        if (book != null && book.progressIndex >= 0) {
            listState.scrollToItem(
                book.progressIndex,
                book.progressOffset
            )
        }
    }

    // сохраняем прогресс при закрытии экрана
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                viewModel.saveProgress(
                    bookId = id,
                    index = listState.firstVisibleItemIndex,
                    offset = listState.firstVisibleItemScrollOffset
                )
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(32.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            val sections: List<ElementUI> = book?.elements.orEmpty()
            items(sections) { section ->
                ComponentUI(section)
            }
        }
        if (viewModel.error.value.isNotBlank()) {
            Toast.makeText(LocalContext.current, viewModel.error.value, Toast.LENGTH_SHORT).show()
        }
        // todo: отрефактоирть все, подписать комментов где надо
        // придумать алгоритм чтения того, где находится прогресс, а не сначала + что-то делать, когда будет двигаться скролл
        Button(
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            modifier = Modifier
                .padding(16.dp)
                .size(64.dp),
            onClick = {
                viewModel.togglePlayPause()
            }
        ) {
            Icon(
                imageVector = viewModel.buttonIcon.value,
                "",
            )
        }
    }
}