package me.alexandervortex.shelfie.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument

private const val ID = "id"
private const val URI = "uri"

fun NavGraphBuilder.getId(): NamedNavArgument {
    return navArgument(ID) { type = NavType.StringType }
}

fun NavGraphBuilder.getUri(): NamedNavArgument {
    return navArgument(URI) {
        type = NavType.StringType
        nullable = true
        defaultValue = null
    }
}
