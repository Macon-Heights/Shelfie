package me.alexandervortex.shelfie.features.navigate

import androidx.navigation.NavBackStackEntry

const val ID = "id"

fun NavBackStackEntry.getId(): String {
    return this.arguments?.getString(ID).orEmpty()
}