package me.alexandervortex.shelfie.ui.component.viewerui

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import me.alexandervortex.shelfie.ui.model.BlockUi

@Composable
fun EmptyLineComponent(model: BlockUi.EmptyLine?) {
    Text(
        "___ ___ ___",
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.background(Color.Red)
    )
}

@Composable
@Preview(
    widthDp = 240,
    heightDp = 320,
    showBackground = true
)
fun PreviewEmptyLineComponent() {
    EmptyLineComponent(null)
}