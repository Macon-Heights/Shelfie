package me.alexandervortex.shelfie.ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

fun getKey(index: Int, firstIndex: Int?): Int {
    return index - (firstIndex ?: 0)
}

sealed interface ElementUIModel {

    data object Skeleton : ElementUIModel

    data class TextUIModel(
        val parts: List<StyledText>,
    ) : ElementUIModel

    data class ImageUIModel(
        val image: ByteArray,
    ) : ElementUIModel {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ImageUIModel

            if (!image.contentEquals(other.image)) return false

            return true
        }

        override fun hashCode(): Int {
            return image.contentHashCode()
        }
    }

    data object EmptyLineUIModel : ElementUIModel

    data class DebugUIModel(
        val type: String, // type of tag
        val message: String, // error text
    )
}

data class StyledText(
    val styles: Set<TextStyleModel>, // enum later
    val text: String,
)

fun composeSpanStyle(
    styles: Set<TextStyleModel>,
    linkColor: Color,
): SpanStyle {
    var span = SpanStyle()

    styles.forEach { style ->
        when (style) {
            TextStyleModel.Bold -> span = span.merge(SpanStyle(fontWeight = FontWeight.Black))
            TextStyleModel.Italic -> span = span.merge(SpanStyle(fontStyle = FontStyle.Italic))
            TextStyleModel.Underline -> span =
                span.merge(SpanStyle(textDecoration = TextDecoration.Underline))

            TextStyleModel.Sub -> span =
                span.merge(SpanStyle(baselineShift = androidx.compose.ui.text.style.BaselineShift.Subscript))

            TextStyleModel.Sup -> span =
                span.merge(SpanStyle(baselineShift = androidx.compose.ui.text.style.BaselineShift.Superscript))

            is TextStyleModel.Link -> span =
                span.merge(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline))

            TextStyleModel.Monospace -> span =
                span.merge(SpanStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace))

            is TextStyleModel.Custom -> when (style.name) {
                "strike" -> span =
                    span.merge(SpanStyle(textDecoration = TextDecoration.LineThrough))

                else -> {}
            }
        }
    }
    return span
}
