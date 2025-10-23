package me.alexandervortex.shelfie.features.mediaviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.ui.component.ActionButtonComponent
import me.alexandervortex.shelfie.ui.theme.IC_PAUSE
import me.alexandervortex.shelfie.ui.theme.IC_PLAY
import me.alexandervortex.shelfie.ui.theme.SHAPE_M
import me.alexandervortex.shelfie.ui.theme.getColors

@Composable
fun ServiceStateComponent(
    state: MediaServiceState,
    playPauseAction: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(
                elevation = 6.dp,
                shape = SHAPE_M,
                clip = false
            )
            .clip(SHAPE_M)
            .background(getColors().primary)
            .padding(16.dp)
    ) {
        Column(
            Modifier.weight(1f)
        ) {
            Text(if (state.isPlaying) "is playing" else "STOPPED", color = getColors().onPrimary)
            Text("${state.index}:${state.part}", color = getColors().onPrimary)
        }
        ActionButtonComponent(
            content = {
                Icon(
                    imageVector = if (state.isPlaying) IC_PAUSE else IC_PLAY,
                    contentDescription = null
                )
            },
            action = { playPauseAction.invoke() }
        )
        Column(
            Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                color = getColors().onPrimary,
                textAlign = TextAlign.End,
                text = state.title.toString()
            )
            Text(
                color = getColors().onPrimary,
                textAlign = TextAlign.End,
                text = state.author.toString()
            )
        }
    }
}

@Preview
@Composable
fun ServiceStateComponentPreview() {
    val state = MediaServiceState.playingState().copy(
        author = "Sashke",
        title = "Blahblah"
    )
    ServiceStateComponent(state) { }
}