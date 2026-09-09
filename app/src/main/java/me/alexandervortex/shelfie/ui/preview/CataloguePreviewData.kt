package me.alexandervortex.shelfie.ui.preview

import me.alexandervortex.shelfie.model.CatalogueItemModel
import me.alexandervortex.shelfie.ui.model.CatalogueItemUIModel
import me.alexandervortex.shelfie.ui.preview.BookPreviewFactory.getTitles

object CataloguePreviewData {

    fun getBooks(): List<CatalogueItemUIModel.Model> {
        return getTitles().map {
            CatalogueItemUIModel.Model(
                data = CatalogueItemModel(
                    id = "id",
                    localPath = "path",
                    title = it.firstOrNull().orEmpty(),
                    author = it.lastOrNull(),
                    year = (1800..2010).random().toString(),
                    scrollIndex = 9,
                    elements = 20
                ),
                isChecked = listOf(true, false).random(),
            )
        }
    }
}