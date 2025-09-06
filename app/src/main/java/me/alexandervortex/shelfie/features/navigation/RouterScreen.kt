package me.alexandervortex.shelfie.features.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import me.alexandervortex.shelfie.features.viewer.ViewerScreen
import me.alexandervortex.shelfie.features.viewer.ViewerViewModel

@Composable
fun RouterScreen() {
    val vm = hiltViewModel<ViewerViewModel>()
    ViewerScreen(vm)
}