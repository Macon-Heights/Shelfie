package me.alexandervortex.shelfie.base.ext

import me.alexandervortex.shelfie.ui.model.ElementUIModel

fun ElementUIModel?.orEmpty(): List<ElementUIModel> {
    return this?.let { listOf(it) } ?: emptyList()
}