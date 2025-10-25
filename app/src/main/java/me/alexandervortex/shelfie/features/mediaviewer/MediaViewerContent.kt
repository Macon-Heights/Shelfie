package me.alexandervortex.shelfie.features.mediaviewer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.ui.model.ElementUI

@Composable
fun MediaViewerContent(
    isMenu: Boolean,
    book: BookUI?,
    serviceState: MediaServiceState,
    listState: LazyListState,
    playPauseAction: () -> Unit,
    timerAction: () -> Unit,
    speedAction: () -> Unit,
    textAction: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        // region UI
        LazyColumn(
            userScrollEnabled = !serviceState.isPlaying,
            state = listState,
            contentPadding = PaddingValues(16.dp),
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
                        .size(16.dp)
                        .clickable { textAction.invoke() },
                )
            }
        }
        // endregion
        if (isMenu) {
            ServiceStateComponent(
                state = serviceState,
                playPauseAction = { playPauseAction.invoke() },
                timerAction = { timerAction.invoke() },
                speedAction = { speedAction.invoke() }
            )
        }
    }
}