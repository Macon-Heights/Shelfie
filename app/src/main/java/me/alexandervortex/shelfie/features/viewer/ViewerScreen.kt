package me.alexandervortex.shelfie.features.viewer

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import me.alexandervortex.shelfie.ui.component.ComponentUI
import me.alexandervortex.shelfie.ui.component.FABComponent
import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.theme.IC_ADD

// todo: отрефактоирть все, подписать комментов где надо
// придумать алгоритм чтения того, где находится прогресс, а не сначала + что-то делать, когда будет двигаться скролл

@Composable
fun ViewerScreen(
    viewModel: ViewerViewModel,
    id: String,
) {
    val listState = rememberLazyListState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val book = viewModel.bookModel.value

    // загружаем книгу
    LaunchedEffect(true) { viewModel.loadCurrentBook(id) }

    // после загрузки книги → восстанавливаем позицию
    LaunchedEffect(book?.progressIndex, book?.progressOffset) {
        book?.let {
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

    // region интерфейс
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        // элементы книги
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(32.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            val sections: List<ElementUI> = book?.elements.orEmpty()
            items(sections) { section ->
                ComponentUI(section)
            }
        }
        // показывалка ошибки
        if (viewModel.error.value.isNotBlank()) {
            Toast.makeText(LocalContext.current, viewModel.error.value, Toast.LENGTH_SHORT).show()
        }

        // плей-пауза кнопка
        FABComponent(viewModel.buttonIcon?.value ?: IC_ADD) {
            viewModel.togglePlayPause(
                listState.firstVisibleItemIndex,
                listState.firstVisibleItemScrollOffset
            )
        }
    }
    // endregion
}