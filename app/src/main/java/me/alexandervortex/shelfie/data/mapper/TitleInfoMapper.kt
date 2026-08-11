package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.ui.component.new.ImageUIModel
import me.alexandervortex.shelfie.ui.model.TitleInfoUIModel
import org.jsoup.nodes.Element
import javax.inject.Inject

class TitleInfoMapper
@Inject constructor() {

    fun map(
        titleInfo: Element?,
        coverImage: ImageUIModel?,
        manyImages: List<ImageUIModel?>
    ): TitleInfoUIModel {
        val result = TitleInfoUIModel(
            title = (titleInfo?.selectFirst("book-title") ?: titleInfo?.selectFirst("title"))?.text()?.trim(),
            date = titleInfo?.selectFirst("date")?.text()?.trim(),
            author = titleInfo?.selectFirst("author")?.let {
                val first = it.selectFirst("first-name")?.text()?.trim()
                val last = it.selectFirst("last-name")?.text()?.trim()
                "$first $last".trim()
            },
            annotation = titleInfo?.selectFirst("annotation")?.text()?.trim(),
            genre = titleInfo?.selectFirst("genre")?.text()?.trim(),
            lang = titleInfo?.selectFirst("lang")?.text()?.trim(),
            coverImage = coverImage,
            manyImages = manyImages
        )
        return result
    }
}