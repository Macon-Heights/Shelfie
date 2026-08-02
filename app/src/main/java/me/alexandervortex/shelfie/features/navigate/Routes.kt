package me.alexandervortex.shelfie.features.navigate

import android.net.Uri

data class AddBookRoute(
    val uri: Uri
) {
    val route = "addbook?uri=${Uri.encode(uri.toString())}"

    companion object {
         val route = "addbook?uri={uri}"
    }
}

data object CatalogueRoute {

    val route = "catalogue"
}

data class MediaViewerRoute(
    val id: String,
) {

    val route = "viewer?id=${id}"
    val uriPattern = "https://shelfie.com/book/${id}"

    companion object {

        val route = "viewer?id={id}"
        val uriPattern = "https://shelfie.com/book/{id}"
    }
}
