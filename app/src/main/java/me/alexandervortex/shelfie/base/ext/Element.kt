package me.alexandervortex.shelfie.base.ext

import me.alexandervortex.shelfie.ui.model.ElementUI
import org.jsoup.nodes.Element

fun Element.isPrimitive(): Boolean {
    return this.childNodes().isEmpty()
}

fun ElementUI?.orEmpty(): List<ElementUI> {
    return this?.let { listOf(it) } ?: emptyList()
}