package me.alexandervortex.shelfie.ui.preview

import me.alexandervortex.shelfie.features.player.MediaServiceState
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.ElementUIModel
import me.alexandervortex.shelfie.ui.model.StyledText
import me.alexandervortex.shelfie.ui.model.TitleInfoUIModel

fun getBookUI(): BookUIModel {
    return BookUIModel(
        titleInfo = getTitleInfo(),
        elements = getElements(),
        progressIndex = 0,
        progressOffset = 0
    )
}

fun playingState(): MediaServiceState {
    return MediaServiceState(
        isPlaying = true,
    )
}

fun pausedState(): MediaServiceState {
    return MediaServiceState(
        isPlaying = false,
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
    return listOf(
        ElementUIModel.Skeleton,
        ElementUIModel.Skeleton,
        ElementUIModel.Skeleton,
        ElementUIModel.TextUIModel(
            parts = listOf(
                StyledText(
                    styles = emptySet(),
                    text = "Whereas the peoples of the United Nations have in the Charter reaffirmed their faith in fundamental human rights, in the dignity and worth of the human person and in the equal rights of men and women and have determined to promote social progress and better standards of life in larger freedom."
                ),
                StyledText(
                    styles = emptySet(),
                    text = "Whereas Member States have pledged themselves to achieve, in co‐operation with the United Nations, the promotion of universal respect for and observance of human rights and fundamental freedoms."
                ),
                StyledText(
                    styles = emptySet(),
                    text = "Whereas a common understanding of these rights and freedoms is of the greatest importance for the full realization of this pledge."
                )
            )
        ),
        ElementUIModel.EmptyLineUIModel,
        ElementUIModel.TextUIModel(
            parts = listOf(
                StyledText(
                    styles = emptySet(),
                    text = "Whereas the peoples of the United Nations have in the Charter reaffirmed their faith in fundamental human rights, in the dignity and worth of the human person and in the equal rights of men and women and have determined to promote social progress and better standards of life in larger freedom."
                ),
                StyledText(
                    styles = emptySet(),
                    text = "Whereas Member States have pledged themselves to achieve, in co‐operation with the United Nations, the promotion of universal respect for and observance of human rights and fundamental freedoms."
                ),
                StyledText(
                    styles = emptySet(),
                    text = "Whereas a common understanding of these rights and freedoms is of the greatest importance for the full realization of this pledge."
                )
            )
        ),
    )
}