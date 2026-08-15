package me.alexandervortex.shelfie.feature.catalogue

import me.alexandervortex.shelfie.model.CatalogueItemModel
import me.alexandervortex.shelfie.ui.model.CatalogueItemUIModel
import javax.inject.Inject

class CatalogueUIFactory
@Inject constructor() {

    fun getCatalogueItemUIModel(
        model: CatalogueItemModel
    ): CatalogueItemUIModel.Model {
        return CatalogueItemUIModel.Model(
            isChecked = false,
            data = model,
        )
    }
}