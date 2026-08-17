package me.alexandervortex.shelfie.feature.viewer

import me.alexandervortex.shelfie.model.ProgressBookModel
import me.alexandervortex.shelfie.ui.model.BookUIModel
import javax.inject.Inject

class ViewerUIFactory
@Inject constructor() {

    fun getBookUIModel(
        model: ProgressBookModel?
    ): BookUIModel? {
        return model?.let {
            BookUIModel(
                id = it.id,
                localPath = it.localPath,
                titleInfo = it.book.titleInfo,
                elements = it.book.elements,
                progressIndex = it.progress.progressIndex,
                progressOffset = it.progress.progressOffset
            )
        }
    }
}