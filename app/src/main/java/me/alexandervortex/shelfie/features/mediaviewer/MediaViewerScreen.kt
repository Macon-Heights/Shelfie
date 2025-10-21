package me.alexandervortex.shelfie.features.mediaviewer

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import me.alexandervortex.shelfie.features.viewer.TAG

@Composable
// Это только плеер, листалка будет отдельным компонентом положена сверху
fun MediaViewerScreen(
    // id есть - это клик по книге в каталоге
    id: String,
    // должен загружать книгу и передавать в сервис сразу же, больше ничиво (ttsvm)
    mediaViewerViewModel: MediaViewerViewModel,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Log.d("${TAG}_MediaViewerScreen", "loadCurrentBook:${id}")
        mediaViewerViewModel.loadCurrentBook(id)
        mediaViewerViewModel.bindService(context)
    }
}