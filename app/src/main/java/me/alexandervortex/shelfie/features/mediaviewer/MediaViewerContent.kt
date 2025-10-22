package me.alexandervortex.shelfie.features.mediaviewer

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.features.mediaplayer.ServiceStateComponent
import me.alexandervortex.shelfie.features.viewer.TAG
import me.alexandervortex.shelfie.ui.component.ActionButtonComponent
import me.alexandervortex.shelfie.ui.component.ComponentUI
import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.theme.IC_PAUSE
import me.alexandervortex.shelfie.ui.theme.IC_PLAY

@Composable
fun MediaViewerContent(
    book: BookUI?,
    serviceState: MediaServiceState,
    listState: LazyListState,
    playPauseAction: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        // region UI
        Log.d("${TAG}_ViewerScreen", "recomposition:${book?.elements?.size}")
        LazyColumn(
            userScrollEnabled = !serviceState.isPlaying,
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
        ServiceStateComponent(serviceState)
        ActionButtonComponent(
            content = {
                Icon(
                    imageVector = if (serviceState.isPlaying) IC_PAUSE else IC_PLAY,
                    contentDescription = null
                )
            },
            action = { playPauseAction.invoke() }
        )
    }
}