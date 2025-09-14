package me.alexandervortex.shelfie.features.filepicker

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun FilePickerScreen(
    navController: NavHostController,
    viewModel: FilePickerViewModel,
) {
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
        viewModel?.onBookAdded(uri, context)
    }

    LaunchedEffect(true) { viewModel?.loadBooks() }
    LazyColumn {
        item {
            Spacer(modifier = Modifier.size(64.dp))
            Text("Твои книги")
        }
        items(viewModel?.books.orEmpty()) { item ->
            Text(item.title, Modifier.clickable {
                viewModel?.setCurrentBook(item)
                navController?.navigate("viewer")
            })
        }

        item {
            Button({
                picker.launch(arrayOf("text/*", "application/*", "application/octet-stream"))
            }) { Text("Добавить") }
        }
    }
}