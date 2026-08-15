package me.alexandervortex.shelfie.feature.catalogue

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import me.alexandervortex.shelfie.feature.catalogue.mvi.CatalogueEffect
import me.alexandervortex.shelfie.feature.catalogue.mvi.CatalogueIntent
import me.alexandervortex.shelfie.feature.navigation.AddBookRoute
import me.alexandervortex.shelfie.feature.navigation.MediaViewerRoute

@Composable
fun CatalogueScreen(
    vm: CatalogueViewModel,
    nav: NavHostController,
) {
    val context = LocalContext.current
    val state by vm.state.collectAsStateWithLifecycle()

    BackHandler(enabled = state.isRemoveMode) {
        vm.onIntent(CatalogueIntent.ToggleRemoveMode())
    }

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
        onBookOpen = { nav.navigate(MediaViewerRoute(it.data.id).route) },
        onToggleBookCheck = { vm.onIntent(CatalogueIntent.ToggleBookCheck(it.data.id)) },
        onToggleRemoveMode = { vm.onIntent(CatalogueIntent.ToggleRemoveMode(it.data.id)) },
        onAddClick = { picker.launch(arrayOf("text/*", "application/*")) },
        onDeleteClick = { vm.onIntent(CatalogueIntent.RemoveChecked) },
        onTogglePopup = { vm.onIntent(CatalogueIntent.TogglePopup(it)) }
    )
}