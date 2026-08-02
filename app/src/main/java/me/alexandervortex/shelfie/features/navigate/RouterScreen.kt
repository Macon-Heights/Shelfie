package me.alexandervortex.shelfie.features.navigate

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import me.alexandervortex.shelfie.features.mvi.addbook.AddBookScreen
import me.alexandervortex.shelfie.features.mvi.addbook.AddBookViewModel
import me.alexandervortex.shelfie.features.mvi.catalogue.CatalogueScreen
import me.alexandervortex.shelfie.features.mvi.catalogue.CatalogueViewModel
import me.alexandervortex.shelfie.features.mvi.viewer.ViewerScreen
import me.alexandervortex.shelfie.features.mvi.viewer.ViewerViewModel

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
            AddBookScreen(
                uri = uri,
                viewModel = hiltViewModel<AddBookViewModel>(),
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