package me.alexandervortex.shelfie.features.catalogue

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import me.alexandervortex.shelfie.base.BaseCatalogueViewModel

@Composable
fun CatalogueScreen(
    navController: NavHostController,
    vm: BaseCatalogueViewModel,
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
    LazyColumn(modifier = Modifier.padding(32.dp)) {
        item {
            Text(
                text = "Your\nBooks",
                lineHeight = 72.sp,
                fontSize = 72.sp,
                fontWeight = FontWeight.Black,
            )
        }
        items(vm.books) { item ->
            Text(item.title, Modifier.clickable {
                vm.setCurrentBook(item)
                navController.navigate("viewer")
            })
        }
        item {
            Button({
                // fixme
                picker.launch(arrayOf("text/*", "application/*", "application/octet-stream"))
            }) { Text("Добавить") }
        }
    }
}