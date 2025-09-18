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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import me.alexandervortex.shelfie.base.BaseCatalogueViewModel
import me.alexandervortex.shelfie.ui.component.BookComponent
import me.alexandervortex.shelfie.ui.component.TitleComponent

@Composable
fun CatalogueScreen(
    vm: BaseCatalogueViewModel,
    navController: NavHostController? = null,
) {
    // region fixme
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
        vm.addBookByUri(uri, context)
    }
    // endregion
    LaunchedEffect(true) { vm.loadBooks() }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            Modifier
                .padding(horizontal = 32.dp)
                .fillMaxSize()
        ) {
            TitleComponent(
                text = "Your\nBooks",
                modifier = Modifier.padding(vertical = 128.dp)
            )
            LazyVerticalGrid(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                columns = GridCells.Adaptive(120.dp),
                modifier = Modifier,
            ) {
                items(vm.books) { item ->
                    BookComponent(item, Modifier.clickable {
                        vm.setCurrentBook(item)
                        navController?.navigate("viewer")
                    })
                }
            }
        }
        Button(
            modifier = Modifier
                .padding(16.dp)
                .size(64.dp),
            onClick = {
                picker.launch(arrayOf("text/*", "application/*", "application/octet-stream"))
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                "",
                tint = Color.White
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CatalogueScreen() {
    CatalogueScreen(vm = hiltViewModel<CataloguePreviewViewModel>())
}