import android.net.Uri

sealed interface CatalogueIntent {
    data object LoadBooks : CatalogueIntent
    data class ImportBook(val uri: Uri) : CatalogueIntent
    data class ToggleRemoveMode(val id: String) : CatalogueIntent
    data class ToggleBookCheck(val id: String) : CatalogueIntent
    data object RemoveChecked : CatalogueIntent
}