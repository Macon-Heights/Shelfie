package me.alexandervortex.shelfie.features.navigate

import androidx.navigation.NavBackStackEntry

private const val ID = "id"
private const val URI = "uri"

fun NavBackStackEntry.getId(): String {
    return this.arguments?.getString(ID).orEmpty()
}

fun NavBackStackEntry.getUri(): String {
    return this.arguments?.getString(URI).orEmpty()
}