package me.alexandervortex.shelfie.features.mediaviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.base.ext.clipNShadow
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.ui.component.getBookUI
import me.alexandervortex.shelfie.ui.component.refactored.ButtonComponent
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_NEXT
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_PAUSE
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_PLAY
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_PREV
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_SPEED
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_TIMER
import me.alexandervortex.shelfie.ui.theme.SHAPE_TOP_L

private const val BUTTON_BIG = 64
private const val BUTTON_SMALL = 48

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
            .clipNShadow(SHAPE_TOP_L)
            .background(getColors().surfaceVariant)
            .padding(top = 12.dp)
            .padding(horizontal = 12.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val playPauseIcon = if (state.isPlaying) IC_PLAYER_PAUSE else IC_PLAYER_PLAY

            ButtonWithLabel(IC_PLAYER_PREV) { prevAction.invoke() }
            ButtonWithLabel(IC_PLAYER_TIMER, state.timer.text) { timerAction.invoke() }
            ButtonComponent(
                modifierAfter = Modifier
                    .size(BUTTON_BIG.dp)
                    .clickable { playPauseAction.invoke() },
                content = {
                    Icon(
                        imageVector = playPauseIcon,
                        contentDescription = null,
                        tint = it
                    )
                }
            )
            ButtonWithLabel(IC_PLAYER_SPEED, state.speed.text) { speedAction.invoke() }
            ButtonWithLabel(IC_PLAYER_NEXT) { nextAction.invoke() }
        }
        ProgressLine(index, elements)
        Spacer(Modifier.size(8.dp))
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
        ButtonComponent(
            containerColor = getColors().secondary,
            contentColor = getColors().onSecondary,
            modifierAfter = Modifier
                .size(BUTTON_SMALL.dp)
                .clickable { playPauseAction.invoke() },
            content = {
                Icon(
                    imageVector = playPauseIcon,
                    contentDescription = null,
                    tint = it
                )
            }
        )
        if (text.isNullOrEmpty().not()) {
            Spacer(Modifier.size(8.dp))
            Text(
                color = getColors().onSurfaceVariant,
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
                    index = 3,
                ),
            listState = LazyListState(),
            nextAction = {},
            prevAction = {},
            textAction = {},
            speedAction = {},
            timerAction = {},
            playPauseAction = {}
        )
    }
}