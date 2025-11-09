package me.alexandervortex.shelfie.ui.component.refactored

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews

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

@CombinedPreviews
@Composable
private fun PopupBoxPreview() {
    CombinedPreviews {
        PopupBox(
            modifier = Modifier.size(320.dp),
            contentAlignment = Alignment.Center,
            content = {
                Text(
                    text = "CONTENT",
                    fontSize = 120.sp,
                    lineHeight = 120.sp
                )
            },
            popup = {
                ConfirmationComponent(
                    "Kek", "Lol", "Nu da", "Nononono", {}
                ) { }
            }
        )
    }
}