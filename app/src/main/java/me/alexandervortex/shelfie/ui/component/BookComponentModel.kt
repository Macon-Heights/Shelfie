package me.alexandervortex.shelfie.ui.component

data class BookComponentModel(
    // UI
    val isChecked: Boolean?,
    // Book Entity
    val id: String,
    val localPath: String,
    val title: String?,
    val author: String?,
    val year: String?,
    val scrollIndex: Int = 0,
    val scrollOffset: Int = 0,
    val elements: Int = 0,
)