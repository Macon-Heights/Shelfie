package me.alexandervortex.shelfie.features.navigate

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import me.alexandervortex.shelfie.features.catalogue.CatalogueScreen
import me.alexandervortex.shelfie.features.catalogue.CatalogueViewModel
import me.alexandervortex.shelfie.features.mediaplayer.TtsViewModel
import me.alexandervortex.shelfie.features.viewer.ViewerBookViewModel
import me.alexandervortex.shelfie.features.viewer.ViewerScreen

@Composable
fun RouterScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CatalogueRoute.route
    ) {

        composable(
            route = CatalogueRoute.route
        ) {
            CatalogueScreen(
                viewModel = hiltViewModel<CatalogueViewModel>(),
                navController = navController
            )
        }

        composable(
            route = ViewerRoute.route,
            arguments = listOf(getId()),
            deepLinks = listOf(navDeepLink {
                uriPattern = ViewerRoute.uriPattern
            }),
        ) {
            val bookVm = hiltViewModel<ViewerBookViewModel>()
            val ttsVm = hiltViewModel<TtsViewModel>()

            ViewerScreen(
                id = it.getId(),
                viewModel = bookVm,
                ttsVm = ttsVm
            )
        }
    }
}