package me.alexandervortex.shelfie.features.mediaviewer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.ui.component.getBookUI
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.SHAPE_L
import me.alexandervortex.shelfie.ui.theme.SHAPE_M
import me.alexandervortex.shelfie.ui.theme.getColors

@CombinedPreviews
@Composable
fun MediaViewerPreview32() {
    CombinedPreviews {
        val bookUI = getBookUI()
        MediaViewerContent(
            isMenu = true,
            book = bookUI,
            serviceState = MediaServiceState.playingState()
                .copy(
                    title = "Title",
                    speed = SpeechRate.FAST,
                    timer = TimerValue.MIN_20
                ),
            listState = LazyListState(),
            nextAction = {},
            textAction = {},
            prevAction = {},
            timerAction = {},
            speedAction = {},
            playPauseAction = {},
        )
    }
}

@Composable
fun RoundButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    isPrimary: Boolean = false,
    action: () -> Unit,
) {
    val size = if (isPrimary) 64 else 48
    val colorsPrimary = ButtonColors(
        containerColor = getColors().primary,
        contentColor = getColors().onPrimary,
        disabledContainerColor = getColors().primary,
        disabledContentColor = getColors().onPrimary
    )
    val colorsDefault = ButtonColors(
        containerColor = getColors().secondary,
        contentColor = getColors().onSecondary,
        disabledContainerColor = getColors().secondary,
        disabledContentColor = getColors().onSecondary
    )
    val colors = if (isPrimary) colorsPrimary else colorsDefault
    Button(
        shape = SHAPE_M,
        contentPadding = PaddingValues(0.dp),
        colors = colors,
        modifier = modifier
            .size(size.dp)
            .shadow(
                elevation = 8.dp,
                shape = SHAPE_L,
                clip = false
            )
            .clip(SHAPE_L),
        onClick = action
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}