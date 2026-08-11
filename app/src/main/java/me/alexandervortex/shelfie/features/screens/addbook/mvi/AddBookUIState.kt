package me.alexandervortex.shelfie.features.screens.addbook.mvi

data class AddBookUIState(
    val title: String?,
    val author: String?,
    val annotation: String?,
    val date: String?,
    val genre: String?,
    val lang: String?,
    val coverImage: BasicImage?,
    val gallery: List<BasicImage?>
)