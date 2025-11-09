package me.alexandervortex.shelfie.ui.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.features.settings.LocalAppSettings
import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.model.composeSpanStyle
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews

@Composable
fun ComponentUI(
    modifier: Modifier = Modifier,
    element: ElementUI,
    currentIndex: Int,
    elementIndex: Int,
    partIndex: Int,
) {
    when (element) {
        is ElementUI.TextUI -> {
            val linkColor = MaterialTheme.colorScheme.primary

            val styledText = buildAnnotatedString {
                element.parts.forEachIndexed { wordIndex, word ->
                    val isCurrentElement = elementIndex == currentIndex
                    val isCurrentWord = wordIndex == partIndex
                    val isHighlight = isCurrentElement && isCurrentWord

                    val baseTextColor = if (isHighlight)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onBackground

                    val baseBgColor = if (isHighlight)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        Color.Transparent

                    val span = composeSpanStyle(word.styles, linkColor)
                        .merge(SpanStyle(color = baseTextColor, background = baseBgColor))

                    withStyle(span) { append(word.text) }
                }
            }
            val fontSize = LocalAppSettings.fontSize.current.sp
            Text(
                text = styledText,
                fontSize = fontSize,
                lineHeight = 32.sp,
                textAlign = TextAlign.Justify,
                modifier = modifier.padding(bottom = 32.dp)
            )
        }

        is ElementUI.ImageUI -> {
            val bitmap = BitmapFactory.decodeByteArray(
                element.image, 0, element.image.size
            )

            bitmap?.let {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                )
            }
        }

        is ElementUI.EmptyLineUI -> {
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )
        }
    }
}

@CombinedPreviews
@Composable
fun TextUiPreview() {
    CombinedPreviews {
        ComponentUI(
            element = getBookUI().elements.first(),
            elementIndex = 0,
            currentIndex = 0,
            partIndex = 2
        )
    }
}