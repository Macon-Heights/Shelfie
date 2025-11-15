package me.alexandervortex.shelfie.ui.model

sealed interface TextStyleModel {
    data object Bold : TextStyleModel
    data object Italic : TextStyleModel
    data object Underline : TextStyleModel
    data object Sub : TextStyleModel
    data object Sup : TextStyleModel
    data class Link(val href: String) : TextStyleModel
    data object Monospace : TextStyleModel
    data class Custom(val name: String) : TextStyleModel
}