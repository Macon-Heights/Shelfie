package me.alexandervortex.shelfie.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.ui.theme.getColors

private const val size = 64
private const val spacing = 8

@Composable
fun TitleComponent(
    text: String?,
    modifier: Modifier = Modifier,
) {
    text?.let {
        Text(
            textAlign = TextAlign.End,
            color = getColors().onBackground,
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
    TitleComponent(stringResource(R.string.catalogue_title))
}