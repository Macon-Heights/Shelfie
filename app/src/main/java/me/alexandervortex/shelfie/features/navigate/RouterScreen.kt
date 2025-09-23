package me.alexandervortex.shelfie.features.navigate

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import me.alexandervortex.shelfie.features.catalogue.CatalogueScreen
import me.alexandervortex.shelfie.features.catalogue.CatalogueViewModel
import me.alexandervortex.shelfie.features.viewer.ViewerScreen
import me.alexandervortex.shelfie.features.viewer.ViewerViewModel

@Composable
fun RouterScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "catalogue"
    ) {
        composable(
            route = Viewer.route,
            arguments = listOf(getId()),
            deepLinks = listOf(navDeepLink {
                uriPattern = Viewer.uriPattern
            }),
        ) {

            ViewerScreen(hiltViewModel<ViewerViewModel>(), it.getId())
        }
        composable("catalogue") {
            CatalogueScreen(hiltViewModel<CatalogueViewModel>(), navController)
        }
    }
}

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

