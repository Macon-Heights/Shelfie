package me.alexandervortex.shelfie.features.screens.addbook.mvi

import me.alexandervortex.shelfie.ui.model.BasicImage

fun getBookUIState(
    coverImage: BasicImage? = null,
    gallery: List<BasicImage> = emptyList()
): AddBookUIState {
    return AddBookUIState(
        title = "Frankenstein",
        date = "1818",
        author = "Mary Shelley",
        annotation = "Victor Frankenstein, a young scientist driven by ambition, discovers a way to create life. However, he is horrified by the creature he brings into the world and abandons it. Rejected by society and desperate for companionship, the creature turns against its creator. The novel explores responsibility, loneliness, prejudice, and the consequences of uncontrolled ambition.",
        genre = "Gothic Fiction",
        lang = "en",
        coverImage = coverImage,
        gallery = gallery
    )
}