package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.ui.model.TitleInfoUI
import org.jsoup.nodes.Element
import javax.inject.Inject

class TitleInfoMapper
@Inject constructor() {

    fun map(
        id: String,
        localPath: String,
        titleInfo: Element?,
    ): TitleInfoUI {
        return TitleInfoUI(
            id = id,
            localPath = localPath,
            title = titleInfo?.selectFirst("book-title")?.text()?.trim(),
            year = titleInfo?.selectFirst("date")?.text()?.trim(),
            author = titleInfo?.selectFirst("author")?.let {
                val first = it.selectFirst("first-name")?.text()?.trim().orEmpty()
                val last = it.selectFirst("last-name")?.text()?.trim().orEmpty()
                "$first $last"
            },
            annotation = titleInfo?.selectFirst("annotation")?.text()?.trim(),
            genre = titleInfo?.selectFirst("genre")?.text()?.trim(),
            coverImage = null // todo later
        )
    }
}