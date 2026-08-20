package me.alexandervortex.shelfie.ui.model.new

fun getKey(index: Int, firstIndex: Int?): Int {
    return index - (firstIndex ?: 0)
}

@Deprecated("remove asap")
sealed interface ElementUIModel {

    data object Skeleton : ElementUIModel

    data class TextUIModel(
        val parts: List<StyledText>,
    ) : ElementUIModel

    data object EmptyLineUIModel : ElementUIModel
}