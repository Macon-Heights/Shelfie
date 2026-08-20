package me.alexandervortex.shelfie.base.ext

import me.alexandervortex.shelfie.ui.model.new.UI

@Deprecated("remove asap")
fun UI?.orEmpty(): List<UI> {
    return this?.let { listOf(it) } ?: emptyList()
}