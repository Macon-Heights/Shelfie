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
            title = "",
            year = "",
            author = "",
            annotation = "",
            genre = "",
            coverImage = null
        )
        // todo titleinfo to variables
    }
}