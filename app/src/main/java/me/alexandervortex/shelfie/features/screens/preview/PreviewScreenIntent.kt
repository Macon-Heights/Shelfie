package me.alexandervortex.shelfie.features.screens.preview

import android.net.Uri

sealed interface PreviewScreenIntent {
    data class Add(val uri: Uri?) : PreviewScreenIntent
    data class Import(val uri: Uri?) : PreviewScreenIntent
}