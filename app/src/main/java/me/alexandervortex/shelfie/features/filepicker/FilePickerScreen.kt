package me.alexandervortex.shelfie.features.filepicker

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
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

//        TODO: viewModel.onPicked(uri)
    }

    Button({
        picker.launch(arrayOf("text/*", "application/*", "application/octet-stream"))
    }) { Text("Выбрать FB2 книгу") }
}