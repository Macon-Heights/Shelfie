package me.alexandervortex.shelfie.features.mediaviewer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.ui.component.ComponentUI
import me.alexandervortex.shelfie.ui.component.getBookUI
import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews

@Composable
fun MediaViewerContent(
    isMenu: Boolean,
    book: BookUI?,
    serviceState: MediaServiceState,
    listState: LazyListState,
    playPauseAction: () -> Unit,
    timerAction: () -> Unit,
    speedAction: () -> Unit,
    prevAction: () -> Unit,
    nextAction: () -> Unit,
    textAction: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        LazyColumn(
            userScrollEnabled = !serviceState.isPlaying,
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            val sections: List<ElementUI> = book?.elements.orEmpty()
            itemsIndexed(sections) { index, section ->
                ComponentUI(
                    modifier = Modifier.clickable { textAction.invoke() },
                    element = section,
                    elementIndex = index,
                    currentIndex = serviceState.index,
                    partIndex = serviceState.part
                )
                Spacer(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { textAction.invoke() },
                )
            }
        }
        Column {
            if (isMenu) {
                MediaStateComponent(
                    state = serviceState,
                    index = listState.firstVisibleItemIndex,
                    elements = book?.elements?.size ?: 0,
                    playPauseAction = { playPauseAction.invoke() },
                    timerAction = { timerAction.invoke() },
                    speedAction = { speedAction.invoke() },
                    prevAction = { prevAction.invoke() },
                    nextAction = { nextAction.invoke() }
                )
            }
        }
    }
}

@CombinedPreviews
@Composable
fun MediaViewerPreview() {
    CombinedPreviews {
        val bookUI = getBookUI()
        MediaViewerContent(
            isMenu = true,
            book = bookUI,
            serviceState = MediaServiceState.playingState(),
            listState = LazyListState(),
            nextAction = {},
            playPauseAction = {},
            prevAction = {},
            textAction = {},
            speedAction = {},
            timerAction = {},
        )
    }
}