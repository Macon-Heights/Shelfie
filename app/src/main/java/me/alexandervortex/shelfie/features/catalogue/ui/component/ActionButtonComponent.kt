package me.alexandervortex.shelfie.features.catalogue.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.IC_ADD
import me.alexandervortex.shelfie.ui.theme.SHAPE_L
import me.alexandervortex.shelfie.ui.theme.SHAPE_M
import me.alexandervortex.shelfie.ui.theme.getColors

private const val CONTENT_DESCRIPTION = ""

@Composable
@CombinedPreviews
fun ActionButtonComponentPreview() {
    CombinedPreviews {
        ActionButtonComponent(
            content = {
                Icon(imageVector = IC_ADD, contentDescription = "")
            },
            action = { }
        )
    }
}

@Composable
fun ActionButtonComponent(
    content: @Composable () -> Unit,
    action: (() -> Unit)? = {},
) {
    val colors = ButtonColors(
        containerColor = getColors().primaryContainer,
        contentColor = getColors().onPrimaryContainer,
        disabledContainerColor = getColors().primaryContainer,
        disabledContentColor = getColors().onPrimaryContainer,
    )

    Button(
        shape = SHAPE_M,
        colors = colors,
        modifier = Modifier
            .padding(32.dp)
            .size(64.dp)
            .shadow(
                elevation = 8.dp,
                shape = SHAPE_L,
                clip = false
            )
            .clip(SHAPE_L),
        onClick = { action?.invoke() }
    ) {
        content.invoke()
    }
}