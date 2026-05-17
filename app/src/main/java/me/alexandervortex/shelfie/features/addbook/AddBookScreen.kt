package me.alexandervortex.shelfie.features.addbook

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import me.alexandervortex.shelfie.ui.component.TitleUI

@Composable
fun AddBookScreen(
    uri: Uri?,
    viewModel: AddBookViewModel,
    navController: NavHostController,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TitleUI("$uri")
}