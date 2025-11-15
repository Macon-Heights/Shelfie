package me.alexandervortex.shelfie.features.viewer

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

@Composable
fun ViewerScreen(
    id: String,
    viewModel: ViewerViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val serviceState = state.serviceState

    val book = state.book
    val context = LocalContext.current
    val listState = rememberLazyListState()

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        viewModel.loadCurrentBook(id)
        viewModel.bindService(context)
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
            viewModel.unbindService(context)
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
        isMenu = state.isMenuVisible,
        book = book,
        serviceState = serviceState,
        listState = listState,
        playPauseAction = {
            val topIndex = listState.firstVisibleItemIndex
            viewModel.togglePlayPause(topIndex)
        },
        timerAction = {
            viewModel.clickTimer()
        },
        speedAction = {
            viewModel.clickSpeed()
        },
        textAction = {
            viewModel.toggleMenu(isMenuVisible = !state.isMenuVisible)
        },
        nextAction = {
            viewModel.clickNext()
        },
        prevAction = {
            viewModel.clickPrev()
        }
    )
}