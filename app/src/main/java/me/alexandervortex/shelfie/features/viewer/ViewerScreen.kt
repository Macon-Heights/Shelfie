package me.alexandervortex.shelfie.features.viewer

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import me.alexandervortex.shelfie.R
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
    viewModel: BaseViewerViewModel,
    ttsVm: TtsViewModel,
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val book = viewModel.bookModel.value

    LaunchedEffect(true) {
        Log.d("${TAG}_ViewerScreen", "loadCurrentBook:${id}")
        viewModel.loadCurrentBook(id)
    }

    LaunchedEffect(book) {
        Log.d("${TAG}_ViewerScreen", "loadBook:${book?.elements?.size}")
        ttsVm.loadBook(book) // ttsViewModel.initTTSWithBook(it)
//        ttsVm.state.value.index = book.progressIndex
//        ttsVm.state.value.part = book.progressOffset

        // old
        viewModel.bookModel.value?.let {
            listState.animateScrollToItem(ttsVm.state.value.index)
        }
    }

    // state из сервиса реактивно
    val serviceState by ttsVm.state.collectAsStateWithLifecycle()

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

    // UI
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
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
            }
        }

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

        ActionButtonComponent(
            content = {
                Icon(
                    imageVector = if (serviceState.buttonIconRes == R.drawable.ic_pause) IC_PAUSE else IC_PLAY,
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
