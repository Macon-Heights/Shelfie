package me.alexandervortex.shelfie.ui.model.new

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import me.alexandervortex.shelfie.model.ImageModel
import me.alexandervortex.shelfie.ui.model.TextStyleUIModel

sealed interface UI {

    // basic
    data object EmptyLine : UI

    data class Image(
        val image: ImageModel,
    ) : UI

    data class RichText(
        val parts: List<StyledText>,
    ) : UI

    //

    data object Skeleton : UI

    data class Heading(
        val level: Int,
        val content: RichText,
    ) : UI
}

data class StyledText(
    val styles: Set<TextStyleUIModel>,
    val text: String,
)

fun composeSpanStyle(
    styles: Set<TextStyleUIModel>,
    linkColor: Color,
): SpanStyle {
    var span = SpanStyle()

    styles.forEach { style ->
        when (style) {
            TextStyleUIModel.Bold -> span = span.merge(SpanStyle(fontWeight = FontWeight.Black))
            TextStyleUIModel.Italic -> span = span.merge(SpanStyle(fontStyle = FontStyle.Italic))
            is TextStyleUIModel.Strikethrough -> span =
                span.merge(SpanStyle(textDecoration = TextDecoration.LineThrough))

            TextStyleUIModel.Underline -> span =
                span.merge(SpanStyle(textDecoration = TextDecoration.Underline))

            TextStyleUIModel.Sub -> span =
                span.merge(SpanStyle(baselineShift = BaselineShift.Subscript))

            TextStyleUIModel.Sup -> span =
                span.merge(SpanStyle(baselineShift = BaselineShift.Superscript))

            is TextStyleUIModel.Link -> span =
                span.merge(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline))

            TextStyleUIModel.Monospace -> span =
                span.merge(SpanStyle(fontFamily = FontFamily.Monospace))

            is TextStyleUIModel.Custom -> when (style.name) {
                "strike" -> span =
                    span.merge(SpanStyle(textDecoration = TextDecoration.LineThrough))

                else -> {}
            }
        }
    }
    return span
}