package me.alexandervortex.shelfie.features.mediaviewer

import android.widget.Toast
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/*
    Это только плеер, листалка будет отдельным компонентом положена сверху
*/


@Composable
fun MediaViewerScreen(
    id: String,
    ttsVm: MediaViewerViewModel,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val serviceState by ttsVm.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val book = ttsVm.bookUI.value

    // загрузить книг
    // подключиться к сервису
    // подскроллить под место
    // region book work
    LaunchedEffect(Unit) {
        ttsVm.loadCurrentBook(id)
        ttsVm.bindService(context)
    }

    LaunchedEffect(book) {
        book?.let {
            listState.scrollToItem(
                index = ttsVm.state.value.index,
                scrollOffset = 0
            )
        }
    }
    // endregion

    // показать тост если ошибка есть в вм или сервисе
    // region errors
    LaunchedEffect(serviceState.error) {
        if (serviceState.error.isNotBlank()) {
            Toast.makeText(context, serviceState.error, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(serviceState.error) {
        if (serviceState.error.isNotBlank()) {
            Toast.makeText(context, serviceState.error, Toast.LENGTH_SHORT).show()
        }
    }
    // endregion

    // по выходу с экрана сохраняю прогресс (надо сохранять еще по паузе в сервисе)
    // region save state onStop
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                ttsVm.saveScrollStateOnDispose(
                    id = id,
                    index = listState.firstVisibleItemIndex,
                    offset = listState.firstVisibleItemScrollOffset
                )
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            ttsVm.unbindService(context)
        }
    }
    // endregion

    // скроллим туда где идет воспроизведение
    // region autoscroll
    LaunchedEffect(serviceState.index, serviceState.part) {
        if (serviceState.isPlaying) {
            try {
                listState.animateScrollToItem(serviceState.index, scrollOffset = 0)
            } catch (e: Exception) {

            }
        }
    }
    // endregion

    MediaViewerContent(
        book = book,
        serviceState = serviceState,
        listState = listState,
        playPauseAction = {
            val topIndex = listState.firstVisibleItemIndex
            ttsVm.togglePlayPause(topIndex)
        },
        timerAction = {

        },
        speedAction = {

        }
    )
}