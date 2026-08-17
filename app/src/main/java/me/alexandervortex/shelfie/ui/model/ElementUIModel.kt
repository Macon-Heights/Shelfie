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
        val image: ImageModel,
    ) : ElementUIModel

    data object EmptyLineUIModel : ElementUIModel

    data class DebugUIModel(
        val type: String, // type of tag
        val message: String, // error text
    )
}

data class StyledText(
    val styles: Set<TextStyleUIModel>, // enum later
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
            TextStyleUIModel.Underline -> span =
                span.merge(SpanStyle(textDecoration = TextDecoration.Underline))

            TextStyleUIModel.Sub -> span =
                span.merge(SpanStyle(baselineShift = androidx.compose.ui.text.style.BaselineShift.Subscript))

            TextStyleUIModel.Sup -> span =
                span.merge(SpanStyle(baselineShift = androidx.compose.ui.text.style.BaselineShift.Superscript))

            is TextStyleUIModel.Link -> span =
                span.merge(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline))

            TextStyleUIModel.Monospace -> span =
                span.merge(SpanStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace))

            is TextStyleUIModel.Custom -> when (style.name) {
                "strike" -> span =
                    span.merge(SpanStyle(textDecoration = TextDecoration.LineThrough))

                else -> {}
            }
        }
    }
    return span
}
