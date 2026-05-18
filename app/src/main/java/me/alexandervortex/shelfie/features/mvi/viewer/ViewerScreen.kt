package me.alexandervortex.shelfie.features.mvi.viewer

import android.widget.Toast
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.alexandervortex.shelfie.features.mvi.viewer.mvi.ViewerIntent

@Composable
fun ViewerScreen(
    id: String,
    viewModel: ViewerViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    val serviceState = state.serviceState
    val book = state.book

    LaunchedEffect(id) {
        viewModel.onIntent(ViewerIntent.LoadBook(id))
        viewModel.onIntent(ViewerIntent.BindService(context))
    }

    LaunchedEffect(book) {
        book?.let {
            listState.scrollToItem(
                index = serviceState.index,
                scrollOffset = 0
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

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onIntent(
                ViewerIntent.SaveScrollStateOnDispose(
                    id = id,
                    index = listState.firstVisibleItemIndex,
                    offset = listState.firstVisibleItemScrollOffset
                )
            )
            viewModel.onIntent(ViewerIntent.UnbindService(context))
        }
    }

    LaunchedEffect(serviceState.index, serviceState.part) {
        if (serviceState.isPlaying) {
            try {
                listState.animateScrollToItem(serviceState.index, scrollOffset = 0)
            } catch (e: Exception) {

            }
        }
    }

    ViewerContent(
        state = state,
        listState = listState,
        onIntent = viewModel::onIntent
    )
}