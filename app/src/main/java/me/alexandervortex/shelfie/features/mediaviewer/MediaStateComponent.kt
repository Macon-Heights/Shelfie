package me.alexandervortex.shelfie.features.mediaviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_NEXT
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_PAUSE
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_PLAY
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_PREV
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_SPEED
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_TIMER
import me.alexandervortex.shelfie.ui.theme.SHAPE_L
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
            .padding(bottom = 12.dp)
            .padding(8.dp)
            .shadow(
                elevation = 6.dp,
                shape = SHAPE_L,
                clip = false
            )
            .clip(SHAPE_L)
            .background(getColors().surfaceBright)
            .padding(8.dp),
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
        Spacer(Modifier.size(8.dp))
        ProgressLine()
        Spacer(Modifier.size(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val playPauseIcon = if (state.isPlaying) IC_PLAYER_PAUSE else IC_PLAYER_PLAY
            RoundButton(IC_PLAYER_TIMER) { timerAction.invoke() }
            RoundButton(IC_PLAYER_PREV) { prevAction.invoke() }
            RoundButton(playPauseIcon, true) { playPauseAction.invoke() }
            RoundButton(IC_PLAYER_NEXT) { nextAction.invoke() }
            RoundButton(IC_PLAYER_SPEED) { speedAction.invoke() }
        }
    }
}

@CombinedPreviews
@Composable
fun ServiceStateComponentPreview() {
    CombinedPreviews {
        val state = MediaServiceState.pausedState().copy(
            author = "Sashke",
            title = "Blahblah"
        )
        MediaStateComponent(state, {}, {}, {}, {}, {})
    }
}