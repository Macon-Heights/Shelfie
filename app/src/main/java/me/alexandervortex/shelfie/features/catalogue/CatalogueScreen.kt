package me.alexandervortex.shelfie.features.catalogue

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import me.alexandervortex.shelfie.features.catalogue.base.BaseCatalogueViewModel
import me.alexandervortex.shelfie.features.catalogue.component.ActionButtonComponent
import me.alexandervortex.shelfie.features.catalogue.preview.CataloguePreviewViewModel
import me.alexandervortex.shelfie.ui.component.BookComponent
import me.alexandervortex.shelfie.ui.component.TitleComponent
import me.alexandervortex.shelfie.ui.theme.IC_ADD

@Composable
fun CatalogueScreen(
    viewModel: BaseCatalogueViewModel,
    navController: NavHostController? = null,
) {
    // region fixme это бы куда-нибудь вынести по-хорошему потом
    val context = LocalContext.current
    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult

        try {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (e: SecurityException) {
            /* провайдер мог не дать persist */
        }
        viewModel.importFromUri(uri)
    }
    // endregion

    LaunchedEffect(true) { viewModel.getBookEntities() }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            TitleComponent(
                text = "Your\nBooks",
                modifier = Modifier.padding(vertical = 128.dp)
            )
            LazyVerticalStaggeredGrid(
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                columns = StaggeredGridCells.Adaptive(180.dp),
                modifier = Modifier,
            ) {
                items(viewModel.books) { item ->
                    BookComponent(item, Modifier.clickable {
                        navController?.navigate("viewer?id=${item.id}")
                    })
                }
            }
        }
        ActionButtonComponent(IC_ADD) {
            picker.launch(arrayOf("text/*", "application/*", "application/octet-stream"))
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CatalogueScreen() {
    CatalogueScreen(viewModel = hiltViewModel<CataloguePreviewViewModel>())
}