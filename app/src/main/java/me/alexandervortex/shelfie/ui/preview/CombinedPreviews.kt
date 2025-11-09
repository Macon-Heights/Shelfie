package me.alexandervortex.shelfie.ui.preview

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.ui.theme.ShelfieTheme

@Preview(
    name = "Light Mode",
    showBackground = true,
)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class CombinedPreviews

@Composable
fun CombinedPreviews(
    content: @Composable () -> Unit,
) {
    ShelfieTheme {
        Surface(color = getColors().background) { content.invoke() }
    }
}
