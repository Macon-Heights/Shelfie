package me.alexandervortex.shelfie.ui.model

sealed interface ElementUI {

    data class TextUI(
        // paragraph, quote, title, poem
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
    val style: TextStyle, // enum later
    val text: String,
)