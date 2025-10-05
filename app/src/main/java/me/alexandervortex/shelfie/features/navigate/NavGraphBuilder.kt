package me.alexandervortex.shelfie.features.navigate

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument

private const val ID = "id"

fun NavGraphBuilder.getId(): NamedNavArgument {
    return navArgument(ID) { type = NavType.StringType }
}