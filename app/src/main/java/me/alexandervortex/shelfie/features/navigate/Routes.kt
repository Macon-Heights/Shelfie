package me.alexandervortex.shelfie.features.navigate

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
