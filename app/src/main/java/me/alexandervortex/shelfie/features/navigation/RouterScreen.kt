package me.alexandervortex.shelfie.features.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.alexandervortex.shelfie.features.catalogue.CatalogueScreen
import me.alexandervortex.shelfie.features.catalogue.CatalogueViewModel
import me.alexandervortex.shelfie.features.viewer.ViewerScreen
import me.alexandervortex.shelfie.features.viewer.ViewerViewModel
import me.alexandervortex.shelfie.features.welcome.WelcomeScreen
import me.alexandervortex.shelfie.features.welcome.WelcomeViewModel

@Composable
fun RouterScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "catalogue"
    ) {
        composable("welcome") { WelcomeScreen(navController,hiltViewModel<WelcomeViewModel>()) }
        composable("viewer") { ViewerScreen(navController,hiltViewModel<ViewerViewModel>()) }
        composable("catalogue") { CatalogueScreen(navController, hiltViewModel<CatalogueViewModel>()) }
    }
}