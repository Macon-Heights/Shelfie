package me.alexandervortex.shelfie.ui.model

import me.alexandervortex.shelfie.model.CatalogueItemModel

sealed interface CatalogueItemUIModel {

    data class Model(
        // UI
        val isChecked: Boolean,
        // Book Entity
        val data: CatalogueItemModel,
    ) : CatalogueItemUIModel

    data object Skeleton : CatalogueItemUIModel
}