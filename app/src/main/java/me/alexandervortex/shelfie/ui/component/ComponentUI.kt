package me.alexandervortex.shelfie.ui.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.feature.preview.COVER_HEIGHT
import me.alexandervortex.shelfie.feature.settings.LocalAppSettings
import me.alexandervortex.shelfie.feature.viewer.ViewerPreviewData.getBookUI
import me.alexandervortex.shelfie.ui.component.new.ImageUI
import me.alexandervortex.shelfie.ui.component.new.TitleUI
import me.alexandervortex.shelfie.ui.model.UI
import me.alexandervortex.shelfie.ui.model.composeSpanStyle
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.SHAPE_S

@Composable
fun ComponentUI(
    modifier: Modifier = Modifier,
    element: UI,
    isCurrentElement: Boolean,
    partIndex: Int,
) {
    val animatedColor by rememberInfiniteTransition(label = "skeleton").animateColor(
        initialValue = getColors().surfaceVariant,
        targetValue = getColors().surfaceContainer,
        animationSpec = infiniteRepeatable(
            animation = tween(DefaultDurationMillis * 3, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeleton_color"
    )

    val fontSize = LocalAppSettings.fontSize.current
    val lineHeight = LocalAppSettings.lineHeight.current
    when (element) {
        is UI.Heading -> {
            val level = (1f / element.level) + 1
            TitleUI(
                size = (fontSize * level).toInt(),
                modifier = modifier
                    .fillMaxWidth(),
                text = getStyledText(element.content, isCurrentElement, partIndex)
            )
        }

        is  UI.ComplexText -> {
            Text(
                text = getStyledText(element, isCurrentElement, partIndex),
                fontSize = fontSize.sp,
                lineHeight = (fontSize * (1 + lineHeight)).sp,
                modifier = modifier.padding(bottom = 32.dp)
            )
        }

        is UI.Image -> {
            ImageUI(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(COVER_HEIGHT.dp),
                model = element.image
            )
        }

        is UI.EmptyLine -> {
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = (fontSize * 2).dp)
            )
        }

        UI.Skeleton -> {
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
                                .background(animatedColor)
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

@Composable
fun getStyledText(
    element: UI.ComplexText, isCurrentElement: Boolean, partIndex: Int
): AnnotatedString {
    val linkColor = getColors().primary
    val onBackground = getColors().onBackground
    val onPrimaryContainer = getColors().onPrimaryContainer
    val primaryContainer = getColors().primaryContainer

    val styledText = remember(
        element,
        isCurrentElement,
        partIndex,
        linkColor,
        onBackground,
        onPrimaryContainer,
        primaryContainer
    ) {
        buildAnnotatedString {
            element.parts.forEachIndexed { wordIndex, word ->
                val isHighlight = isCurrentElement && wordIndex == partIndex

                val baseTextColor = if (isHighlight) onPrimaryContainer
                else onBackground

                val baseBgColor = if (isHighlight) primaryContainer
                else Color.Transparent

                val span = composeSpanStyle(word.styles, linkColor).merge(
                    SpanStyle(
                        color = baseTextColor,
                        background = baseBgColor
                    )
                )

                withStyle(span) { append(word.text) }
            }
        }
    }
    return styledText
}

@CombinedPreviews
@Composable
fun TextUiPreview() {
    CombinedPreviews {
        getBookUI().elements.forEach {
            ComponentUI(
                element = it,
                isCurrentElement = true,
                partIndex = 2
            )
        }
    }
}