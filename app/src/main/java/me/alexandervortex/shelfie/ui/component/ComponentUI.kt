package me.alexandervortex.shelfie.ui.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.features.settings.LocalAppSettings
import me.alexandervortex.shelfie.ui.model.ElementUIModel
import me.alexandervortex.shelfie.ui.model.composeSpanStyle
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.preview.getBookUI
import me.alexandervortex.shelfie.ui.theme.SHAPE_S

@Composable
fun ComponentUI(
    modifier: Modifier = Modifier,
    element: ElementUIModel,
    currentIndex: Int,
    elementIndex: Int,
    partIndex: Int,
) {
    val fontSize = LocalAppSettings.fontSize.current
    val lineHeight = LocalAppSettings.lineHeight.current
    when (element) {
        is ElementUIModel.TextUIModel -> {
            val linkColor = getColors().primary

            val styledText = buildAnnotatedString {
                element.parts.forEachIndexed { wordIndex, word ->
                    val isCurrentElement = elementIndex == currentIndex
                    val isCurrentWord = wordIndex == partIndex
                    val isHighlight = isCurrentElement && isCurrentWord

                    val baseTextColor = if (isHighlight)
                        getColors().onPrimaryContainer
                    else
                        getColors().onBackground

                    val baseBgColor = if (isHighlight)
                        getColors().primaryContainer
                    else
                        Color.Transparent

                    val span = composeSpanStyle(word.styles, linkColor)
                        .merge(SpanStyle(color = baseTextColor, background = baseBgColor))

                    withStyle(span) { append(word.text) }
                }
            }
            Text(
                text = styledText,
                fontSize = fontSize.sp,
                lineHeight = (fontSize * (1 + lineHeight)).sp, // fixme better formula needed
                textAlign = TextAlign.Justify,
                modifier = modifier.padding(bottom = 32.dp)
            )
        }

        is ElementUIModel.ImageUIModel -> {
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

        is ElementUIModel.EmptyLineUIModel -> {
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = (fontSize * 2).dp)
            )
        }

        ElementUIModel.Skeleton -> {
            Column {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((fontSize * (1 + lineHeight)).dp)
                    ) {
                        Text(
                            text = " ",
                            fontSize = fontSize.sp,
                            textAlign = TextAlign.Justify,
                            modifier = modifier
                                .fillMaxWidth()
                                .clip(SHAPE_S)
                        )
                    }
                }
                Spacer(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(bottom = (fontSize * 2).dp)
                )
            }
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
        ComponentUI(
            element = getBookUI().elements.get(1),
            elementIndex = 0,
            currentIndex = 0,
            partIndex = 2
        )
    }
}