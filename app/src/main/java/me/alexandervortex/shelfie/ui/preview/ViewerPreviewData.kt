package me.alexandervortex.shelfie.ui.preview

import me.alexandervortex.shelfie.feature.player.MediaServiceState
import me.alexandervortex.shelfie.model.BookDocument
import me.alexandervortex.shelfie.model.BookNode
import me.alexandervortex.shelfie.model.GroupKind
import me.alexandervortex.shelfie.model.ImageModel
import me.alexandervortex.shelfie.model.InlineNode
import me.alexandervortex.shelfie.model.PreviewBookModel
import me.alexandervortex.shelfie.model.RichText
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.StyledText
import me.alexandervortex.shelfie.ui.model.UI

object ViewerPreviewData {

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

    fun getBookUI(): BookUIModel {
        return BookUIModel(
            id = "mock_book",
            localPath = "fake_path",
            titleInfo = getTitleInfo(),
            elements = getElements(),
            progressIndex = 0,
            progressOffset = 0
        )
    }

    fun getBookDocument() = BookDocument(
        children = listOf(
            BookNode.Group(
                id = "id1", kind = GroupKind.Other(sourceTag = "span"),
                children = listOf(
                    BookNode.Paragraph(
                        id = "node:root/0/1/1/5", content = RichText(
                            parts = listOf(
                                InlineNode.Text(
                                    text = "Это тот старикан, который раньше каждый год приезжал на ярмарку в В.",
                                    marks = emptySet(),
                                    link = null
                                ),
                                InlineNode.Text(
                                    text = "[1]",
                                    marks = emptySet(),
                                    link = "ch2.xhtml#id49"
                                ),
                                InlineNode.Text(
                                    text = ", привозил на продажу калган-траву и горечавку. Под конец заговорили о вампирах, здухачах",
                                    marks = emptySet(),
                                    link = null
                                ),
                                InlineNode.Text(
                                    text = "[2]",
                                    marks = emptySet(),
                                    link = "ch2.xhtml#id48"
                                ),
                                InlineNode.Text(
                                    text = ", джиннах, ведьмах и о другой нечистой силе, какая только может в глухую ночь человеку явиться.",
                                    marks = emptySet(),
                                    link = null
                                )
                            )
                        )
                    )
                )
            )
        )
    )
}

fun getTitleInfo(
    coverBytes: ImageModel? = null,
    manyImages: List<ImageModel> = emptyList()
): PreviewBookModel {
    return PreviewBookModel(
        title = "Frankenstein",
        date = "1818",
        author = "Mary Shelley",
        annotation = "Victor Frankenstein, a young scientist driven by ambition, discovers a way to create life. However, he is horrified by the creature he brings into the world and abandons it. Rejected by society and desperate for companionship, the creature turns against its creator. The novel explores responsibility, loneliness, prejudice, and the consequences of uncontrolled ambition.",
        genre = "Gothic Fiction",
        lang = "en",
        coverImage = coverBytes,
        gallery = manyImages
    )
}


private fun getElements(): List<UI> {
    return listOf(
        UI.Heading(1, UI.ComplexText(listOf(StyledText(emptySet(), "title one")))),
        UI.Heading(2, UI.ComplexText(listOf(StyledText(emptySet(), "Title 2")))),
        UI.Heading(4, UI.ComplexText(listOf(StyledText(emptySet(), "Title 4")))),
        UI.Heading(6, UI.ComplexText(listOf(StyledText(emptySet(), "Title 6")))),
        UI.Skeleton,
        UI.ComplexText(
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
        UI.EmptyLine,
        UI.ComplexText(
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