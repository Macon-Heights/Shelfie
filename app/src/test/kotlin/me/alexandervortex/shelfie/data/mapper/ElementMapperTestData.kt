package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.ui.model.ElementUI

object ElementMapperTestData {

    fun paragraphWithTextAndImage(): List<ElementUI> {
        return listOf(
            ElementUI.TextUI(listOf("Перед картинкой — текст.")),
            ElementUI.ImageUI("img1".toByteArray()),
            ElementUI.TextUI(listOf("После картинки — текст.")),
        )
    }
}