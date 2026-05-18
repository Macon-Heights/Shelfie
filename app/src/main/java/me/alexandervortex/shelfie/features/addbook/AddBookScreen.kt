package me.alexandervortex.shelfie.features.addbook

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import me.alexandervortex.shelfie.features.addbook.mvi.AddBookEffect
import me.alexandervortex.shelfie.features.addbook.mvi.AddBookIntent

@Composable
fun AddBookScreen(
    uri: Uri?,
    viewModel: AddBookViewModel,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(AddBookIntent.Add(uri))
        viewModel.effect.collect { effect ->
            when (effect) {
                is AddBookEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()

                is AddBookEffect.NavigateTo ->
                    navController.navigate(effect.route)

                is AddBookEffect.Close ->
                    navController.navigateUp()
            }
        }
    }

    AddBookUI(state) {
        viewModel.onIntent(AddBookIntent.Import(uri))
    }
}