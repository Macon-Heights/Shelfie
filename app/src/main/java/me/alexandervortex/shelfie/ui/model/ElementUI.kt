package me.alexandervortex.shelfie.ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

sealed interface ElementUI {

    data class TextUI(
        val parts: List<StyledText>,
    ) : ElementUI

    data class ImageUI(
        val image: ByteArray,
    ) : ElementUI

    data object EmptyLineUI : ElementUI

    data class DebugUI(
        val type: String, // type of tag
        val message: String, // error text
    )
}

data class StyledText(
    val styles: Set<TextStyle>, // enum later
    val text: String,
)

fun composeSpanStyle(
    styles: Set<TextStyle>,
    linkColor: Color,
): SpanStyle {
    var span = SpanStyle()

    styles.forEach { style ->
        when (style) {
            TextStyle.Bold -> span = span.merge(SpanStyle(fontWeight = FontWeight.Black))
            TextStyle.Italic -> span = span.merge(SpanStyle(fontStyle = FontStyle.Italic))
            TextStyle.Underline -> span =
                span.merge(SpanStyle(textDecoration = TextDecoration.Underline))

            TextStyle.Sub -> span =
                span.merge(SpanStyle(baselineShift = androidx.compose.ui.text.style.BaselineShift.Subscript))

            TextStyle.Sup -> span =
                span.merge(SpanStyle(baselineShift = androidx.compose.ui.text.style.BaselineShift.Superscript))

            is TextStyle.Link -> span =
                span.merge(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline))

            TextStyle.Monospace -> span =
                span.merge(SpanStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace))

            is TextStyle.Custom -> when (style.name) {
                "strike" -> span =
                    span.merge(SpanStyle(textDecoration = TextDecoration.LineThrough))

                else -> {}
            }
        }
    }
    return span
}
