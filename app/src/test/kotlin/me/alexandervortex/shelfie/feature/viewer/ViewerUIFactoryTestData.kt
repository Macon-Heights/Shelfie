package me.alexandervortex.shelfie.feature.viewer

import me.alexandervortex.shelfie.model.PreviewBookModel
import me.alexandervortex.shelfie.ui.model.BookUIModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object ViewerUIFactoryTestData {

    fun String?.toElement(): Element? {
        return this?.let { Jsoup.parse(it.trimIndent()) }
    }

    fun uiComplexEpub(): BookUIModel? = null

    fun uiComplex(): BookUIModel? = null

    fun uiEmpty(): BookUIModel {
        return BookUIModel(
            id = "id", localPath = "path", titleInfo = PreviewBookModel(
                title = "Frankenstein",
                date = "1818",
                author = "Mary Shelley",
                annotation = "Victor Frankenstein, a young scientist driven by ambition, discovers a way to create life. However, he is horrified by the creature he brings into the world and abandons it. Rejected by society and desperate for companionship, the creature turns against its creator. The novel explores responsibility, loneliness, prejudice, and the consequences of uncontrolled ambition.",
                genre = "Gothic Fiction",
                lang = "en",
                coverImage = null,
                gallery = emptyList()
            ), elements = emptyList(),
            progressIndex = 0,
            progressOffset = 0
        )
    }

    fun uiWithText(): BookUIModel? = null
}