package me.alexandervortex.shelfie.ui.model

sealed interface TextStyle {
    object Normal : TextStyle
    object Bold : TextStyle
    object Italic : TextStyle
    object Strikethrough : TextStyle
    object Sub : TextStyle
    object Sup : TextStyle
    object Monospace : TextStyle
    data class Link(val href: String) : TextStyle
    data class Custom(val name: String) : TextStyle
}