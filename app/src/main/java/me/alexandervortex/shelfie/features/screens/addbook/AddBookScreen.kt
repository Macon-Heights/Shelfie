package me.alexandervortex.shelfie.features.screens.addbook

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController

@Composable
fun AddBookScreen(
    uri: Uri?,
    viewModel: AddBookViewModel,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(uri) {
        viewModel.onIntent(PreviewScreenIntent.Add(uri))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PreviewScreenEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()

                is PreviewScreenEffect.NavigateTo ->
                    navController.navigate(effect.route)

                is PreviewScreenEffect.Close ->
                    navController.navigateUp()
            }
        }
    }

    AddBookContent(state) {
        viewModel.onIntent(PreviewScreenIntent.Import(uri))
    }
}