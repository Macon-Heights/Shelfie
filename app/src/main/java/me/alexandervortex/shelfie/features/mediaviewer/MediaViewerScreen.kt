package me.alexandervortex.shelfie.features.mediaviewer

import android.util.Log
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
import me.alexandervortex.shelfie.features.viewer.TAG

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
        Log.d("${TAG}_MediaViewerScreen", "loadCurrentBook:${id}")
        ttsVm.loadCurrentBook(id)
        ttsVm.bindService(context)
    }

    LaunchedEffect(book) {
        Log.d("${TAG}_MediaViewerScreen", "loadBook:${book?.elements?.size}")
        book?.let {
            // fixme:  ttsVm.loadBook(book) ??? WHY??
            listState.animateScrollToItem(
                index = ttsVm.state.value.index,
                scrollOffset = -320
            )
        }
    }
    // endregion

    // показать тост если ошибка есть в вм или сервисе
    // region errors
    LaunchedEffect(serviceState.error) {
        if (serviceState.error.isNotBlank()) {
            Log.e("${TAG}_MediaViewerScreen", "service_error:${serviceState.error}")
            Toast.makeText(context, serviceState.error, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(serviceState.error) {
        if (serviceState.error.isNotBlank()) {
            Log.e("${TAG}_MediaViewerScreen", "viewModel_error:${ttsVm.errorState}")
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
                listState.animateScrollToItem(serviceState.index, scrollOffset = -320)
            } catch (e: Exception) {
                Log.e(TAG, "scroll_to_index_failed:${serviceState.index}", e)
            }
        }
    }
    // endregion

    MediaViewerContent(book, serviceState, listState) {
        val topIndex = listState.firstVisibleItemIndex
        Log.d("${TAG}_ViewerScreen", "action_button_clicked:${topIndex}")
        ttsVm.togglePlayPause(topIndex)
    }
}