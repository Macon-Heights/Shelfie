package me.alexandervortex.shelfie.feature.viewer

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
import me.alexandervortex.shelfie.feature.viewer.mvi.ViewerIntent

@Composable
fun ViewerScreen(
    id: String,
    viewModel: ViewerViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val listState = rememberLazyListState()

    val serviceState = state.serviceState
    val book = state.book

    LaunchedEffect(id) {
        viewModel.onIntent(ViewerIntent.LoadBook(id))
        viewModel.onIntent(ViewerIntent.BindService)
    }

    LaunchedEffect(book) {
        book?.let {
            listState.scrollToItem(
                index = serviceState.index,
                scrollOffset = serviceState.offset
            )
        }
    }

    LaunchedEffect(state.error) {
        if (state.error.isNotBlank()) {
            Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(serviceState.error) {
        if (serviceState.error.isNotBlank()) {
            Toast.makeText(context, serviceState.error, Toast.LENGTH_SHORT).show()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                viewModel.onIntent(
                    ViewerIntent.SaveScrollStateOnDispose(
                        id = id,
                        index = listState.firstVisibleItemIndex,
                        offset = listState.firstVisibleItemScrollOffset
                    )
                )
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.onIntent(
                ViewerIntent.SaveScrollStateOnDispose(
                    id = id,
                    index = listState.firstVisibleItemIndex,
                    offset = listState.firstVisibleItemScrollOffset
                )
            )
            viewModel.onIntent(ViewerIntent.UnbindService)
        }
    }

    LaunchedEffect(serviceState.index, serviceState.offset) {
        if (serviceState.isPlaying) {
            listState.animateScrollToItem(serviceState.index, scrollOffset = serviceState.offset)
        }
    }

    ViewerContent(
        state = state,
        listState = listState,
        onIntent = viewModel::onIntent
    )
}