package me.alexandervortex.shelfie.base.ext

import me.alexandervortex.shelfie.ui.model.new.ElementUIModel

@Deprecated("remove asap")
fun ElementUIModel?.orEmpty(): List<ElementUIModel> {
    return this?.let { listOf(it) } ?: emptyList()
}