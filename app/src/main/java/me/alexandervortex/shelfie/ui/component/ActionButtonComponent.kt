package me.alexandervortex.shelfie.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.base.ext.clipNShadow
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.IC_ADD
import me.alexandervortex.shelfie.ui.theme.SHAPE_L
import me.alexandervortex.shelfie.ui.theme.SHAPE_M

@Composable
@CombinedPreviews
fun ActionButtonComponentPreview() {
    CombinedPreviews {
        ActionButtonComponent(
            content = {
                Icon(imageVector = IC_ADD, contentDescription = null)
            },
            action = { }
        )
    }
}

@Composable
fun ActionButtonComponent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    action: (() -> Unit)? = {},
) {

    Button(
        contentPadding = PaddingValues(0.dp),
        shape = SHAPE_M,
        colors = ButtonColors(
            containerColor = getColors().primaryContainer,
            contentColor = getColors().onPrimaryContainer,
            disabledContainerColor = getColors().primaryContainer,
            disabledContentColor = getColors().onPrimaryContainer
        ),
        modifier = modifier
            .size(64.dp)
            .clipNShadow(SHAPE_L),
        onClick = { action?.invoke() }
    ) { content.invoke() }
}