package me.alexandervortex.shelfie.ui.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.theme.getColors

@Composable
fun ComponentUI(
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
                        isHighlight -> MaterialTheme.colorScheme.background
                        else -> MaterialTheme.colorScheme.onBackground
                    }

                    val bgColor = when {
                        isHighlight -> MaterialTheme.colorScheme.onBackground
                        else -> MaterialTheme.colorScheme.background
                    }

                    withStyle(
                        style = SpanStyle(
                            color = textColor,
                            background = bgColor
                        )
                    ) { append(word) }
                    append(". ")
                }
            }

            Text(
                text = styledText,
                textAlign = TextAlign.Justify,
                modifier = Modifier.fillMaxWidth()
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                )
            }
        }

        is ElementUI.EmptyLine -> {
            Spacer(
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .background(getColors().onBackground)
                    .fillMaxWidth()
                    .height(2.dp)
            )
        }
    }
}

@Preview
@Composable
fun TextUiPreview() {
    MaterialTheme {
        val words = LoremIpsum(500).values.toList()
        val element = ElementUI.TextUI(
            parts = words
        )
        ComponentUI(
            element = element,
            elementIndex = 0,
            currentIndex = 0,
            partIndex = 1
        )
    }
}