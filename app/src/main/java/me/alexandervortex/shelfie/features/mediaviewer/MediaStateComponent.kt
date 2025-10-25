package me.alexandervortex.shelfie.features.mediaviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.ui.component.getBookUI
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_NEXT
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_PAUSE
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_PLAY
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_PREV
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_SPEED
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_TIMER
import me.alexandervortex.shelfie.ui.theme.SHAPE_TOP_L
import me.alexandervortex.shelfie.ui.theme.getColors

@Composable
fun MediaStateComponent(
    state: MediaServiceState,
    playPauseAction: () -> Unit,
    timerAction: () -> Unit,
    speedAction: () -> Unit,
    prevAction: () -> Unit,
    nextAction: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(bottom = 12.dp)
//            .padding(8.dp)
            .shadow(
                elevation = 6.dp,
                shape = SHAPE_TOP_L,
                clip = false
            )
            .clip(SHAPE_TOP_L)
            .background(getColors().surfaceBright)
//            .padding(16.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            fontSize = 14.sp,
            lineHeight = 14.sp,
            text = state.title.orEmpty(),
            color = getColors().onSurface,
        )
        ProgressLine()
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val playPauseIcon = if (state.isPlaying) IC_PLAYER_PAUSE else IC_PLAYER_PLAY
            ButtonWithLabel(IC_PLAYER_PREV) { prevAction.invoke() }
            ButtonWithLabel(IC_PLAYER_TIMER, "30m") { timerAction.invoke() }
            RoundButton(playPauseIcon, true) { playPauseAction.invoke() }
            ButtonWithLabel(IC_PLAYER_SPEED, state.speed.text) { speedAction.invoke() }
            ButtonWithLabel(IC_PLAYER_NEXT) { nextAction.invoke() }
        }
    }
}

@Composable
private fun ButtonWithLabel(
    playPauseIcon: ImageVector,
    text: String? = null,
    playPauseAction: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RoundButton(playPauseIcon) { playPauseAction.invoke() }
        if (text.isNullOrEmpty().not()) {
            Spacer(Modifier.size(8.dp))
            Text(text = text.orEmpty(), fontSize = 14.sp, lineHeight = 14.sp)
        }
    }
}

@CombinedPreviews
@Composable
fun MediaViewerPreview2() {
    CombinedPreviews {
        val bookUI = getBookUI()
        MediaViewerContent(
            isMenu = true,
            book = bookUI,
            serviceState = MediaServiceState.playingState().copy(speed = SpeechRate.FAST),
            listState = LazyListState(),
            {}, {}, {}, {}, {}, {},
        )
    }
}