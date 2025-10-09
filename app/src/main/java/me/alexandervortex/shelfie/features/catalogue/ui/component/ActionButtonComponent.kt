package me.alexandervortex.shelfie.features.catalogue.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.ui.theme.getColors

private const val CONTENT_DESCRIPTION = ""

@Composable
fun ActionButtonComponent(
    ic: ImageVector,
    action: (() -> Unit)? = {},
) {
    val colors = ButtonColors(
        containerColor = getColors().primaryContainer,
        contentColor = getColors().onPrimaryContainer,
        disabledContainerColor = getColors().primaryContainer,
        disabledContentColor = getColors().onPrimaryContainer,
    )
    Button(
        colors = colors,
        modifier = Modifier
            .padding(16.dp)
            .size(64.dp),
        onClick = { action?.invoke() }
    ) {
        Icon(
            imageVector = ic,
            contentDescription = CONTENT_DESCRIPTION,
        )
    }
}