package me.alexandervortex.shelfie.ui.model

sealed interface TextStyle {
    data object Normal : TextStyle
    data object Bold : TextStyle
    data object Italic : TextStyle
    data object Strikethrough : TextStyle
    data object Sub : TextStyle
    data object Sup : TextStyle
    data object Monospace : TextStyle
    data class Link(val href: String) : TextStyle
    data class Custom(val name: String) : TextStyle
}