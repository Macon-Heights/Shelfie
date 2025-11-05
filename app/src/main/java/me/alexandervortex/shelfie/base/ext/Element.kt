package me.alexandervortex.shelfie.base.ext

import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.model.StyledText

fun ElementUI?.orEmpty(): List<ElementUI> {
    return this?.let { listOf(it) } ?: emptyList()
}

fun List<ElementUI>.normalizeEmptyLines(): List<ElementUI> {
    val result = mutableListOf<ElementUI>()
    forEachIndexed { _, element ->
        if (element is ElementUI.EmptyLineUI) {
            if (result.lastOrNull() is ElementUI.EmptyLineUI) return@forEachIndexed
        }
        result += element
    }
    return result
}

fun List<ElementUI>.normalizeEmptyTextUI(): List<ElementUI> {
    val result = mutableListOf<ElementUI>()
    forEachIndexed { _, element ->
        if (element is ElementUI.TextUI) {
            if (element.parts.isEmpty() || element.parts.filter { it.text.isNotBlank() }
                    .isEmpty()) return@forEachIndexed
        }
        result += element
    }
    return result
}

fun List<ElementUI>.splitPartsBySentences(): List<ElementUI> {
    val sentenceRegex = Regex("(?<=[.!?])\\s+")
    return map { element ->
        when (element) {
            is ElementUI.TextUI -> {
                val newParts = element.parts.flatMap { part ->
                    val sentences = part.text.split(sentenceRegex)
                        .map {
                            it.trim()
                                .plus(" ")
                                .replace(Regex("\\s+"), " ")
                        }
                        .filter { it.isNotEmpty() }

                    sentences.map { sentence ->
                        StyledText(
                            styles = part.styles,
                            text = sentence
                        )
                    }
                }
                element.copy(parts = newParts)
            }

            else -> element
        }
    }
}

