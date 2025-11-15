package me.alexandervortex.shelfie.ui.component.refactored

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews

private const val size = 64
private const val spacing = 8

@Composable
fun TitleUI(text: String?) {
    text?.let {
        Text(
            textAlign = TextAlign.End,
            color = getColors().onBackground,
            text = text,
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                .padding(vertical = 64.dp)
                .fillMaxWidth(),
            fontSize = size.sp,
            lineHeight = size.sp,
            letterSpacing = spacing.sp,
            fontWeight = FontWeight.Thin,
        )
    }
}

@CombinedPreviews
@Composable
private fun TitlePreview() {
    CombinedPreviews {
        TitleUI(stringResource(R.string.catalogue_title))
    }
}