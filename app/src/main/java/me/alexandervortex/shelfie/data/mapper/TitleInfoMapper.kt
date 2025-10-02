package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.base.Lg
import me.alexandervortex.shelfie.ui.model.TitleInfoUI
import org.jsoup.nodes.Element
import javax.inject.Inject

class TitleInfoMapper
@Inject constructor() {

    private val lg = Lg("TitleInfoMapper")

    fun map(
        id: String,
        localPath: String,
        titleInfo: Element?,
    ): TitleInfoUI {
        lg.log("map start")
        val result = TitleInfoUI(
            id = id,
            localPath = localPath,
            title = titleInfo?.selectFirst("book-title")?.text()?.trim(),
            date = titleInfo?.selectFirst("date")?.text()?.trim(),
            author = titleInfo?.selectFirst("author")?.let {
                val first = it.selectFirst("first-name")?.text()?.trim()
                val last = it.selectFirst("last-name")?.text()?.trim()
                "$first $last".trim()
            },
            annotation = titleInfo?.selectFirst("annotation")?.text()?.trim(),
            genre = titleInfo?.selectFirst("genre")?.text()?.trim(),
            coverImage = null // todo later
        )
        lg.log("map end")
        return result
    }
}