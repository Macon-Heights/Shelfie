package me.alexandervortex.shelfie.base.ext

import me.alexandervortex.shelfie.ui.model.ElementUIModel
import me.alexandervortex.shelfie.ui.model.StyledText

fun ElementUIModel?.orEmpty(): List<ElementUIModel> {
    return this?.let { listOf(it) } ?: emptyList()
}

fun List<ElementUIModel>.normalizeEmptyLines(): List<ElementUIModel> {
    val result = mutableListOf<ElementUIModel>()
    forEachIndexed { _, element ->
        if (element is ElementUIModel.EmptyLineUIModel) {
            if (result.lastOrNull() is ElementUIModel.EmptyLineUIModel) return@forEachIndexed
        }
        result += element
    }
    return result
}

fun List<ElementUIModel>.normalizeEmptyTextUI(): List<ElementUIModel> {
    val result = mutableListOf<ElementUIModel>()
    forEachIndexed { _, element ->
        if (element is ElementUIModel.TextUIModel) {
            if (element.parts.isEmpty() || element.parts.filter { it.text.isNotBlank() }
                    .isEmpty()) return@forEachIndexed
        }
        result += element
    }
    return result
}

fun List<ElementUIModel>.splitPartsBySentences(): List<ElementUIModel> {
    val sentenceRegex = Regex("(?<=[.!?])\\s+")
    return map { element ->
        when (element) {
            is ElementUIModel.TextUIModel -> {
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

