package me.alexandervortex.shelfie.ui.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.alexandervortex.shelfie.ui.theme.ShelfieTheme
import me.alexandervortex.shelfie.ui.theme.getColors

private const val WIDTH = 300
private const val HEIGHT = 900

@Preview(
    widthDp = WIDTH,
    heightDp = HEIGHT,
    name = "Light Mode",
    showBackground = true
)
@Preview(
    widthDp = WIDTH,
    heightDp = HEIGHT,
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class CombinedPreviews

@Composable
fun CombinedPreviews(
    content: @Composable () -> Unit,
) {
    ShelfieTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = getColors().background
        ) { content.invoke() }
    }
}
