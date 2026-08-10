package me.alexandervortex.shelfie.features.screens.catalogue

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
import me.alexandervortex.shelfie.features.screens.catalogue.mvi.CatalogueEffect
import me.alexandervortex.shelfie.features.screens.catalogue.mvi.CatalogueIntent
import me.alexandervortex.shelfie.features.navigate.AddBookRoute
import me.alexandervortex.shelfie.features.navigate.MediaViewerRoute

@Composable
fun CatalogueScreen(
    vm: CatalogueViewModel,
    nav: NavHostController,
) {
    val context = LocalContext.current
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                is CatalogueEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()

                is CatalogueEffect.NavigateTo ->
                    nav.navigate(effect.route)
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
        nav.navigate(AddBookRoute(uri).route)
    }

    CatalogueContent(
        state = state,
        onBookOpen = { nav.navigate(MediaViewerRoute(it.id).route) },
        onToggleBookCheck = { vm.onIntent(CatalogueIntent.ToggleBookCheck(it.id)) },
        onToggleRemoveMode = { vm.onIntent(CatalogueIntent.ToggleRemoveMode(it.id)) },
        onAddClick = { picker.launch(arrayOf("text/*", "application/*")) },
        onDeleteClick = { vm.onIntent(CatalogueIntent.RemoveChecked) },
        onTogglePopup = { vm.onIntent(CatalogueIntent.TogglePopup(it)) }
    )
}