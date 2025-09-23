package me.alexandervortex.shelfie.features.navigate

data class Viewer(
    val id: String,
) {

    val route = "viewer?id=${id}"
    val uriPattern = "https://shelfie.com/book/${id}"

    companion object {

        val route = "viewer?id={id}"
        val uriPattern = "https://shelfie.com/book/{id}"
    }
}

data object Catalogue {

    val route = "catalogue"
}