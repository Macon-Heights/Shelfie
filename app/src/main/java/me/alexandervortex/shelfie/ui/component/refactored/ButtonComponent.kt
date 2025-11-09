package me.alexandervortex.shelfie.ui.component.refactored

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
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

@Composable
fun ButtonComponent(
    modifierAfter: Modifier = Modifier,
    content: @Composable (Color) -> Unit,
    shape: Shape = SHAPE_L,
    containerColor: Color = getColors().primary,
    contentColor: Color = getColors().onPrimary,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .sizeIn(minHeight = MIN_HEIGHT.dp)
            .clipNShadow(shape)
            .then(modifierAfter)
            .background(containerColor)
    ) { content.invoke(contentColor) }
}

@Composable
@CombinedPreviews
private fun ButtonPreview() {
    CombinedPreviews {
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