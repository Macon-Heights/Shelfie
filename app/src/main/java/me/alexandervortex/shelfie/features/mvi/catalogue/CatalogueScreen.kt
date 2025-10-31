package me.alexandervortex.shelfie.features.mvi.catalogue

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import me.alexandervortex.shelfie.features.mvi.catalogue.mvi.CatalogueEffect
import me.alexandervortex.shelfie.features.mvi.catalogue.mvi.CatalogueIntent
import me.alexandervortex.shelfie.features.navigate.MediaViewerRoute

@Composable
fun CatalogueScreen(
    viewModel: CatalogueViewModel,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(CatalogueIntent.LoadBooks)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CatalogueEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()

                is CatalogueEffect.NavigateTo ->
                    navController.navigate(effect.route)
            }
        }
    }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        try {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (_: SecurityException) {
        }
        viewModel.onIntent(CatalogueIntent.ImportBook(uri))
    }

    CatalogueScreenContent(
        state = state,
        onBookClick = { book ->
            if (state.isRemoveMode)
                viewModel.onIntent(CatalogueIntent.ToggleBookCheck(book.id))
            else
                navController.navigate(MediaViewerRoute(book.id).route)
        },
        onBookLongClick = { viewModel.onIntent(CatalogueIntent.ToggleRemoveMode(it.id)) },
        onAddClick = { picker.launch(arrayOf("text/*", "application/*")) },
        onDeleteClick = { viewModel.onIntent(CatalogueIntent.RemoveChecked) }
    )
}