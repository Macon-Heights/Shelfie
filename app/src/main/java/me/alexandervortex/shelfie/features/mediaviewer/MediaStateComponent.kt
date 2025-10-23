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
import me.alexandervortex.shelfie.ui.theme.SHAPE_L
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
            .padding(8.dp)
            .shadow(
                elevation = 6.dp,
                shape = SHAPE_M,
                clip = false
            )
            .clip(SHAPE_L)
            .background(getColors().primaryContainer)
            .padding(16.dp)
    ) {
        Column {
            Text("isPlaying = ${state.isPlaying}")
            Text(" ")
            Text("index = ${state.index}")
            Text("part = ${state.part}")
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
        Column(horizontalAlignment = Alignment.End) {
            Text(textAlign = TextAlign.End, text = "title = ${state.title}")
            Text(textAlign = TextAlign.End, text = "author = ${state.author}")
            Text(textAlign = TextAlign.End, text = " ")
            Text(textAlign = TextAlign.End, text = "error = ${state.error}")
        }
    }
}

@Preview
@Composable
fun ServiceStateComponentPreview() {
    val state = MediaServiceState.playingState()
    ServiceStateComponent(state) { }
}