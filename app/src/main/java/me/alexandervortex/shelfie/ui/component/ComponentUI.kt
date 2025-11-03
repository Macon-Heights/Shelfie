package me.alexandervortex.shelfie.ui.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.ui.model.ElementUI
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
            val styledText = buildAnnotatedString {
                element.parts.forEachIndexed { wordIndex, word ->

                    val isCurrentElement = elementIndex == currentIndex
                    val isCurrentWord = wordIndex == partIndex
                    val isHighlight = isCurrentElement && isCurrentWord

                    val textColor = when {
                        isHighlight -> MaterialTheme.colorScheme.onPrimaryContainer
                        else -> MaterialTheme.colorScheme.onBackground
                    }

                    val bgColor = when {
                        isHighlight -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.background
                    }

                    withStyle(
                        style = SpanStyle(
                            color = textColor,
                            background = bgColor
                        )
                    ) { append(word.text) }
                }
            }

            Text(
                text = styledText,
                fontSize = 16.sp,
                lineHeight = 32.sp,
                textAlign = TextAlign.Justify,
                modifier = modifier.fillMaxWidth()
            )
        }

        is ElementUI.ImageUI -> {
            val bitmap = BitmapFactory.decodeByteArray(
                element.image, 0, element.image.size
            )

            if (bitmap != null) {
                Image(
                    contentScale = ContentScale.FillWidth,
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                )
            }
        }

        is ElementUI.EmptyLineUI -> {
            Spacer(
                modifier = modifier
                    .size(64.dp)
                    .fillMaxWidth()
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