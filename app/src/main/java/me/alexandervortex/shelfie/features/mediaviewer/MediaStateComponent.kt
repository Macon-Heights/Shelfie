package me.alexandervortex.shelfie.features.mediaviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
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
    index: Int,
    elements: Int,
    playPauseAction: () -> Unit,
    timerAction: () -> Unit,
    speedAction: () -> Unit,
    prevAction: () -> Unit,
    nextAction: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = SHAPE_TOP_L,
                clip = false
            )
            .clip(SHAPE_TOP_L)
            .background(getColors().surfaceVariant)
            .padding(top = 12.dp)
            .padding(horizontal = 12.dp)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val playPauseIcon = if (state.isPlaying) IC_PLAYER_PAUSE else IC_PLAYER_PLAY

            ButtonWithLabel(IC_PLAYER_PREV) { prevAction.invoke() }
            ButtonWithLabel(IC_PLAYER_TIMER, state.timer.text) { timerAction.invoke() }
            RoundButton(icon = playPauseIcon, isPrimary = true) { playPauseAction.invoke() }
            ButtonWithLabel(IC_PLAYER_SPEED, state.speed.text) { speedAction.invoke() }
            ButtonWithLabel(IC_PLAYER_NEXT) { nextAction.invoke() }
        }
        ProgressLine(index, elements)
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
        RoundButton(icon = playPauseIcon) { playPauseAction.invoke() }
        if (text.isNullOrEmpty().not()) {
            Spacer(Modifier.size(8.dp))
            Text(
                color = getColors().onPrimaryContainer,
                text = text.orEmpty(), fontSize = 14.sp, lineHeight = 14.sp
            )
            Spacer(Modifier.size(8.dp))
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
            serviceState = MediaServiceState.playingState()
                .copy(
                    title = "Title",
                    speed = SpeechRate.FAST,
                    timer = TimerValue.MIN_20,
                    index = 5,
                ),
            listState = LazyListState(),
            {}, {}, {}, {}, {}, {},
        )
    }
}