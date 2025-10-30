package me.alexandervortex.shelfie.features.navigate

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import me.alexandervortex.shelfie.features.mediaviewer.MediaViewerScreen
import me.alexandervortex.shelfie.features.mediaviewer.MediaViewerViewModel
import me.alexandervortex.shelfie.features.mvi.catalogue.CatalogueScreen
import me.alexandervortex.shelfie.features.mvi.catalogue.CatalogueViewModel

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
            route = MediaViewerRoute.route,
            arguments = listOf(getId()),
            deepLinks = listOf(navDeepLink {
                uriPattern = MediaViewerRoute.uriPattern
            }),
        ) {

            val ttsVm = hiltViewModel<MediaViewerViewModel>()

            MediaViewerScreen(
                id = it.getId(),
                ttsVm = ttsVm
            )
        }
    }
}