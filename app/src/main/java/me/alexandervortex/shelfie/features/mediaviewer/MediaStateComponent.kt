package me.alexandervortex.shelfie.features.mediaviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.ui.component.ActionButtonComponent
import me.alexandervortex.shelfie.ui.theme.IC_PAUSE
import me.alexandervortex.shelfie.ui.theme.IC_PLAY
import me.alexandervortex.shelfie.ui.theme.IC_SPEED
import me.alexandervortex.shelfie.ui.theme.IC_TIMER
import me.alexandervortex.shelfie.ui.theme.SHAPE_L
import me.alexandervortex.shelfie.ui.theme.SHAPE_M
import me.alexandervortex.shelfie.ui.theme.getColors

@Composable
fun ServiceStateComponent(
    state: MediaServiceState,
    playPauseAction: () -> Unit,
    timerAction: () -> Unit,
    speedAction: () -> Unit,
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
            .background(getColors().primaryContainer)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier.weight(1f)
        ) {
            Text(
                text = state.title.orEmpty(),
                color = getColors().onPrimaryContainer,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.size(8.dp))
            Text(
                color = getColors().onPrimaryContainer,
                fontWeight = FontWeight.Light,
                text = state.author.orEmpty(),
            )
            Spacer(Modifier.size(8.dp))
            Row {
                Text(
                    color = getColors().onPrimaryContainer,
                    fontWeight = FontWeight.Light,
                    text = "30min",
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    color = getColors().onPrimaryContainer,
                    fontWeight = FontWeight.Light,
                    text = "x1.2",
                )
            }
        }
        Spacer(Modifier.size(16.dp))
        ActionButtonComponent(
            content = {
                Icon(
                    imageVector = if (state.isPlaying) IC_PAUSE else IC_PLAY,
                    contentDescription = null
                )
            },
            action = { playPauseAction.invoke() }
        )
        Spacer(Modifier.size(16.dp))
        Button(
            shape = SHAPE_M,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonColors(
                containerColor = getColors().primary,
                contentColor = getColors().onPrimary,
                disabledContainerColor = getColors().primary,
                disabledContentColor = getColors().onPrimary,
            ),
            modifier = Modifier
                .size(48.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = SHAPE_L,
                    clip = false
                )
                .clip(SHAPE_L),
            onClick = { speedAction.invoke() }
        ) {
            Icon(
                imageVector = IC_SPEED,
                contentDescription = null
            )
        }
        Spacer(Modifier.size(16.dp))
        Button(
            shape = SHAPE_M,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonColors(
                containerColor = getColors().primary,
                contentColor = getColors().onPrimary,
                disabledContainerColor = getColors().primary,
                disabledContentColor = getColors().onPrimary,
            ),
            modifier = Modifier
                .size(48.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = SHAPE_L,
                    clip = false
                )
                .clip(SHAPE_L),
            onClick = { timerAction.invoke() }
        ) {
            Icon(
                imageVector = IC_TIMER,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun ServiceStateComponentPreview() {
    val state = MediaServiceState.pausedState().copy(
        author = "Sashke",
        title = "Blahblah"
    )
    ServiceStateComponent(state, {}, {}, {})
}