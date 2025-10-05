package me.alexandervortex.shelfie.features.viewer

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import me.alexandervortex.shelfie.features.catalogue.component.ActionButtonComponent
import me.alexandervortex.shelfie.ui.component.ComponentUI
import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.theme.IC_ADD

@Composable
fun ViewerScreen(
    id: String,
    viewModel: ViewerBookViewModel,
    ttsViewModel: TtsViewModel,
) {
    val listState = rememberLazyListState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val book = viewModel.bookModel.value

    // загружаем книгу
    LaunchedEffect(true) {
        viewModel.loadCurrentBook(id)
    }

    LaunchedEffect(viewModel.bookModel.value) {
        viewModel.bookModel.value?.let {
            ttsViewModel.initTTSWithBook(it)
            ttsViewModel.scrollElementIndex.value = it.progressIndex
            listState.animateScrollToItem(
                ttsViewModel.scrollElementIndex.value
            )
        }
    }

    // сохраняем прогресс при закрытии экрана
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                viewModel.saveScrollStateOnDispose(
                    id = id,
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
            userScrollEnabled = ttsViewModel.isScrollable.value,
            state = listState,
            contentPadding = PaddingValues(32.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            val sections: List<ElementUI> = book?.elements.orEmpty()
            itemsIndexed(sections) { index, section ->
                ComponentUI(
                    element = section,
                    elementIndex = index,
                    currentIndex = ttsViewModel.scrollElementIndex.value,
                    partIndex = ttsViewModel.scrollElementPart.value
                )
            }
        }
        // показывалка ошибки
        if (viewModel.errorState.value.isNotBlank() || ttsViewModel.errorState.value.isNotBlank()) {
            Toast.makeText(
                LocalContext.current,
                viewModel.errorState.value.ifBlank { null } ?: ttsViewModel.errorState.value,
                Toast.LENGTH_SHORT
            ).show()
        }

        // плей-пауза кнопка
        ActionButtonComponent(ttsViewModel.buttonIcon?.value ?: IC_ADD) {
            ttsViewModel.isScrollable.value = !ttsViewModel.isScrollable.value
            // начинаю воспроизведение с первого (второго-третьего? видимого элемента)
            ttsViewModel.togglePlayPause(
                indexToStartPlaying = listState.firstVisibleItemIndex,
            )
        }
    }
    // endregion
}