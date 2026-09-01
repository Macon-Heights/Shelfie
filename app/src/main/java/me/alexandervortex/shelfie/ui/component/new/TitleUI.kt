package me.alexandervortex.shelfie.ui.component.new

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.ui.preview.BookPreviewFactory.getTitles
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews

private const val spacing = 8

@Composable
fun TitleUI(
    modifier: Modifier = Modifier,
    text: AnnotatedString?,
    size: Int = 64,
) {
    text?.let {
        Text(
            textAlign = TextAlign.End,
            color = getColors().onBackground,
            text = text,
            modifier = modifier.fillMaxWidth(),
            fontSize = size.sp,
            lineHeight = size.sp,
            letterSpacing = spacing.sp,
            fontWeight = FontWeight.Thin,
        )
    }
}

@Preview
@Composable
private fun TitlePreview() {
    CombinedPreviews {
        TitleUI(text = AnnotatedString(getTitles().random().first()))
    }
}