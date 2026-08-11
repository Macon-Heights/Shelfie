package me.alexandervortex.shelfie.feature.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import me.alexandervortex.shelfie.feature.preview.PreviewScreen
import me.alexandervortex.shelfie.feature.preview.PreviewScreenViewModel
import me.alexandervortex.shelfie.feature.catalogue.CatalogueScreen
import me.alexandervortex.shelfie.feature.catalogue.CatalogueViewModel
import me.alexandervortex.shelfie.feature.viewer.ViewerScreen
import me.alexandervortex.shelfie.feature.viewer.ViewerViewModel

@Composable
fun RouterScreen(data: Uri? = null) {
    val navController = rememberNavController()

    LaunchedEffect(data) {
        if (data != null) {
            navController.navigate(AddBookRoute(data).route) {
                popUpTo(CatalogueRoute.route) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = CatalogueRoute.route
    ) {

        composable(
            route = AddBookRoute.route,
            arguments = listOf(getUri()),
        ) {
            val uriString = it.getUri()
            val uri = if (uriString.isNotEmpty()) uriString.toUri() else null
            PreviewScreen(
                uri = uri,
                viewModel = hiltViewModel<PreviewScreenViewModel>(),
                navController = navController
            )
        }

        composable(
            route = CatalogueRoute.route
        ) {
            CatalogueScreen(
                vm = hiltViewModel<CatalogueViewModel>(),
                nav = navController
            )
        }

        composable(
            route = MediaViewerRoute.route,
            arguments = listOf(getId()),
            deepLinks = listOf(navDeepLink {
                uriPattern = MediaViewerRoute.uriPattern
            }),
        ) {

            val viewModel = hiltViewModel<ViewerViewModel>()
            ViewerScreen(
                id = it.getId(),
                viewModel = viewModel
            )
        }
    }
}