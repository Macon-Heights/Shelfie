package me.alexandervortex.shelfie.ui.component.viewerui

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import me.alexandervortex.shelfie.ui.model.BlockUi

@Composable
fun PoemComponent(model: BlockUi.Poem) {
    Text("_POEM_", color = MaterialTheme.colorScheme.onBackground,modifier = Modifier.background(
        Color.Red))
}
