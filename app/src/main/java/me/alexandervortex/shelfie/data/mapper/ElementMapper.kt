package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.ui.model.ElementUI
import org.jsoup.nodes.Element
import javax.inject.Inject

class ElementMapper
@Inject constructor() {

    fun map(
        body: Element?,
        binaries: Element?,
    ): List<ElementUI> {
        return emptyList()
    }
}
