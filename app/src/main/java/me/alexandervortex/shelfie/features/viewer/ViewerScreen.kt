package me.alexandervortex.shelfie.features.viewer

import android.widget.Toast
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current
    val serviceState by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val book = viewModel.bookUIModel.value

    var isMenu by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.loadCurrentBook(id)
        viewModel.bindService(context)
    }

    LaunchedEffect(book) {
        book?.let {
            listState.scrollToItem(
                index = state.serviceState.index,
                scrollOffset = 0
            )
        }
    }

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

    LaunchedEffect(state.serviceState.index, state.serviceState.part) {
        if (state.serviceState.isPlaying) {
            try {
                listState.animateScrollToItem(state.serviceState.index, scrollOffset = 0)
            } catch (e: Exception) {

            }
        }
    }

    ViewerContent(
        isMenu = isMenu,
        book = book,
        serviceState = state.serviceState,
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
            isMenu = !isMenu
        },
        nextAction = {
            viewModel.clickNext()
        },
        prevAction = {
            viewModel.clickPrev()
        }
    )
}