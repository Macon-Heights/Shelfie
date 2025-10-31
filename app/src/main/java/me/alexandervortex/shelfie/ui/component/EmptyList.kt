package me.alexandervortex.shelfie.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.ui.theme.getColors

@Composable
fun EmptyList(@StringRes textRes: Int) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start,
        color = getColors().onBackground,
        text = stringResource(textRes),
        fontSize = 32.sp,
        lineHeight = 56.sp,
        fontWeight = FontWeight.Thin,
    )
}