package me.alexandervortex.shelfie.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

private const val size = 72
private const val spacing = 16

@Composable
fun TitleComponent(
    text: String?,
    modifier: Modifier = Modifier,
) {
    text?.let {
        Text(
            color = MaterialTheme.colorScheme.onBackground,
            text = text,
            modifier = modifier,
            fontSize = size.sp,
            lineHeight = size.sp,
            letterSpacing = spacing.sp,
            fontWeight = FontWeight.Thin,
        )
    }
}

@Composable
@Preview
fun TitleComponent() {
    TitleComponent("Your\nBooks")
}