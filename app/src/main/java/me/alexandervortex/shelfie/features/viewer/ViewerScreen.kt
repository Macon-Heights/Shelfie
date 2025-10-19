package me.alexandervortex.shelfie.features.viewer

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.alexandervortex.shelfie.features.mediaplayer.TtsViewModel
import me.alexandervortex.shelfie.ui.component.ActionButtonComponent
import me.alexandervortex.shelfie.ui.component.ComponentUI
import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.theme.IC_PAUSE
import me.alexandervortex.shelfie.ui.theme.IC_PLAY

const val TAG = "^_^"

@Composable
fun ViewerScreen(
    id: String,
    viewModel: ViewerBookViewModel,
    ttsVm: TtsViewModel,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val serviceState by ttsVm.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // autoscroll
    LaunchedEffect(serviceState.index, serviceState.part) {
        if (serviceState.isPlaying) {
            try {
                listState.animateScrollToItem(serviceState.index, scrollOffset = -320)
            } catch (e: Exception) {
                Log.e(TAG, "scroll_to_index_failed:${serviceState.index}", e)
            }
        }
    }

    // region errors
    LaunchedEffect(serviceState.error) {
        if (serviceState.error.isNotBlank()) {
            Log.e("${TAG}_ViewerScreen", "service_error:${serviceState.error}")
            Toast.makeText(context, serviceState.error, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(serviceState.error) {
        if (serviceState.error.isNotBlank()) {
            Log.e("${TAG}_ViewerScreen", "viewModel_error:${viewModel.errorState}")
            Toast.makeText(context, serviceState.error, Toast.LENGTH_SHORT).show()
        }
    }
    // endregion

    // region init book & send to tts
    val book = viewModel.bookModel.value
    LaunchedEffect(Unit) {
        Log.d("${TAG}_ViewerScreen", "loadCurrentBook:${id}")
        viewModel.loadCurrentBook(id)
        ttsVm.bindService(context)
    }

    LaunchedEffect(book) {
        Log.d("${TAG}_ViewerScreen", "loadBook:${book?.elements?.size}")
        book?.let {
            ttsVm.loadBook(book)
            listState.animateScrollToItem(ttsVm.state.value.index, scrollOffset = -320)
        }
    }
    // endregion

    // region save state onStop
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
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            ttsVm.unbindService(context)
        }
    }
    // endregion

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        // region UI
        Log.d("${TAG}_ViewerScreen", "recomposition:${book?.elements?.size}")
        LazyColumn(
            userScrollEnabled = serviceState.isScrollable,
            state = listState,
            contentPadding = PaddingValues(32.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            val sections: List<ElementUI> = book?.elements.orEmpty()
            itemsIndexed(sections) { index, section ->
                ComponentUI(
                    element = section,
                    elementIndex = index,
                    currentIndex = serviceState.index,
                    partIndex = serviceState.part
                )
                Spacer(Modifier.size(32.dp))
            }
        }
        // endregion

        ActionButtonComponent(
            content = {
                Icon(
                    imageVector = if (serviceState.isPlaying) IC_PAUSE else IC_PLAY,
                    contentDescription = null
                )
            },
            action = {
                val topIndex = listState.firstVisibleItemIndex
                Log.d("${TAG}_ViewerScreen", "action_button_clicked:${topIndex}")
                ttsVm.togglePlayPause(topIndex)
            }
        )
    }
}
