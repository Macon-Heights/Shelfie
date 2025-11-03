package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.ui.model.ElementUI

object TestDataModels {

    fun paragraphWithTextAndImage(): List<ElementUI> {
        return listOf(
            ElementUI.TextUI(listOf("Перед картинкой — текст.")),
            ElementUI.ImageUI("img1".toByteArray()),
            ElementUI.TextUI(listOf("После картинки — текст.")),
        )
    }

    fun emptyElementList(): List<ElementUI> {
        return emptyList()
    }
}