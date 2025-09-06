package me.alexandervortex.shelfie

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RouterScreen() {
    val vm = hiltViewModel<MainViewModel>()
    ViewerScreen(vm)
}