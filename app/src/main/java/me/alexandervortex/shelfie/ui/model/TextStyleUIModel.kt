package me.alexandervortex.shelfie.ui.model

sealed interface TextStyleUIModel {
    data object Bold : TextStyleUIModel
    data object Italic : TextStyleUIModel
    data object Underline : TextStyleUIModel
    data object Sub : TextStyleUIModel
    data object Sup : TextStyleUIModel
    data class Link(val href: String) : TextStyleUIModel
    data object Monospace : TextStyleUIModel
    data class Custom(val name: String) : TextStyleUIModel
}