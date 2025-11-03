package me.alexandervortex.shelfie.base.ext

import me.alexandervortex.shelfie.ui.model.ElementUI
import org.jsoup.nodes.Element

fun Element.isNoChildren(): Boolean {
    return this.childNodes().isEmpty()
}

fun ElementUI?.orEmpty(): List<ElementUI> {
    return this?.let { listOf(it) } ?: emptyList()
}

fun Element.isPrimitiveTag(): Boolean {
    return when (tagName()) {
        "image", "empty-line", "p", "v", "subtitle" -> true
        else -> false
    }
}