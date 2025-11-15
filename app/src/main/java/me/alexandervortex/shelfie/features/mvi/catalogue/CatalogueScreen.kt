package me.alexandervortex.shelfie.features.mvi.catalogue

import android.content.Intent
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
        viewModel.effect.collect { effect ->
            when (effect) {
                is CatalogueEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()

                is CatalogueEffect.NavigateTo ->
                    navController.navigate(effect.route)
            }
        }
    }

    val contract = ActivityResultContracts.OpenDocument()
    val picker = rememberLauncherForActivityResult(contract) { uri ->
        uri ?: return@rememberLauncherForActivityResult

        val intent = Intent.FLAG_GRANT_READ_URI_PERMISSION
        try {
            context.contentResolver.takePersistableUriPermission(uri, intent)
        } catch (_: SecurityException) { }
        viewModel.onIntent(CatalogueIntent.ImportBook(uri))
    }

    CatalogueContent(
        state = state,
        onBookOpen = { navController.navigate(MediaViewerRoute(it.id).route) },
        onToggleBookCheck = { viewModel.onIntent(CatalogueIntent.ToggleBookCheck(it.id)) },
        onToggleRemoveMode = { viewModel.onIntent(CatalogueIntent.ToggleRemoveMode(it.id)) },
        onAddClick = { picker.launch(arrayOf("text/*", "application/*")) },
        onDeleteClick = { viewModel.onIntent(CatalogueIntent.RemoveChecked) },
        onTogglePopup = { viewModel.onIntent(CatalogueIntent.TogglePopup(it)) }
    )
}