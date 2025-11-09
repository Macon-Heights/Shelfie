package me.alexandervortex.shelfie.ui.component.refactored

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PopupBox(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.BottomEnd,
    content: @Composable BoxScope.() -> Unit,
    popup: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment,
        content = {
            content.invoke(this)
            popup.invoke(this)
        }
    )
}