package me.alexandervortex.shelfie.features.mediaviewer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.ui.theme.SHAPE_L
import me.alexandervortex.shelfie.ui.theme.SHAPE_M
import me.alexandervortex.shelfie.ui.theme.getColors

@Composable
fun RoundButton(
    icon: ImageVector,
    isPrimary: Boolean = false,
    action: () -> Unit,
) {
    val size = if (isPrimary) 64 else 48
    val colorsPrimary = ButtonColors(
        containerColor = getColors().primaryContainer,
        contentColor = getColors().onPrimaryContainer,
        disabledContainerColor = getColors().primaryContainer,
        disabledContentColor = getColors().onPrimaryContainer
    )
    val colorsDefault = ButtonColors(
        containerColor = getColors().surfaceContainerHigh,
        contentColor = getColors().onSurface,
        disabledContainerColor = getColors().surfaceContainerHigh,
        disabledContentColor = getColors().onSurface
    )
    val colors = if (isPrimary) colorsPrimary else colorsDefault
    Button(
        shape = SHAPE_M,
        contentPadding = PaddingValues(0.dp),
        colors = colors,
        modifier = Modifier
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