package me.alexandervortex.shelfie.ui.component.refactored

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.base.ext.clipNShadow
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.IC_ADD
import me.alexandervortex.shelfie.ui.theme.SHAPE_END_L
import me.alexandervortex.shelfie.ui.theme.SHAPE_L
import me.alexandervortex.shelfie.ui.theme.SHAPE_START_L

private const val CONTENT_PADDING = 16
private const val MIN_HEIGHT = 48

const val BUTTON_BIG = 64
const val BUTTON_SMALL = 48

@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    modifierAfter: Modifier = Modifier,
    content: @Composable (Color) -> Unit,
    shape: Shape = SHAPE_L,
    containerColor: Color? = null,
    contentColor: Color? = null,
) {
    val DEFAULT_CONTAINER_COLOR = getColors().primary
    val DEFAULT_CONTENT_COLOR = getColors().onPrimary

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .sizeIn(minHeight = MIN_HEIGHT.dp)
            .clipNShadow(shape)
            .then(modifierAfter)
            .background(containerColor ?: DEFAULT_CONTAINER_COLOR)
    ) { content.invoke(contentColor ?: DEFAULT_CONTENT_COLOR) }
}

@Composable
@CombinedPreviews
private fun ButtonPreview() {
    CombinedPreviews {
        ButtonComponent(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
                .padding(32.dp),
            modifierAfter = Modifier
                .size(BUTTON_BIG.dp)
                .clickable { },
            content = {
                Icon(
                    imageVector = IC_ADD,
                    contentDescription = null,
                    tint = it
                )
            }
        )
        Row {
            ButtonComponent(
                modifierAfter = Modifier.size(64.dp),
                content = { Icon(IC_ADD, null, tint = it) }
            )
            ButtonComponent(
                modifierAfter = Modifier.weight(1f),
                content = { Text(text = "default button", color = it) }
            )
            ButtonComponent(
                shape = SHAPE_START_L,
                containerColor = getColors().error,
                contentColor = getColors().onError,
                modifierAfter = Modifier,
                content = { Text(text = "-", color = it) }
            )
            ButtonComponent(
                shape = SHAPE_END_L,
                modifierAfter = Modifier,
                content = { Text(text = "+", color = it) }
            )
        }
    }
}