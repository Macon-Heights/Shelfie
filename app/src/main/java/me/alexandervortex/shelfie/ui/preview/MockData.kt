package me.alexandervortex.shelfie.ui.preview

import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.ui.model.ElementUIModel
import me.alexandervortex.shelfie.ui.model.TitleInfoUIModel

fun getBookUI(): BookUI {
    return BookUI(
        titleInfo = getTitleInfo(),
        elements = getElements(),
        progressIndex = 0,
        progressOffset = 0
    )
}

private fun getTitleInfo(): TitleInfoUIModel {
    return TitleInfoUIModel(
        id = "mock_book",
        localPath = "fake_path",
        title = "Mock Book",
        date = "2025",
        author = "Sashke Vortex",
        annotation = "Mock Book\nThis is just description for a book. This is just description for a book.\n\nby Sashke Vortex",
        genre = "Horror",
        lang = "en",
        coverImage = null
    )
}

private fun getElements(): List<ElementUIModel> {
    return emptyList()
}