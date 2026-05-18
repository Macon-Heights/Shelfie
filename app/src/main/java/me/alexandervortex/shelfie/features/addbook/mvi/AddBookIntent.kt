package me.alexandervortex.shelfie.features.addbook.mvi

import android.net.Uri

sealed interface AddBookIntent {
    data class Add(val uri: Uri?) : AddBookIntent
    data class Import(val uri: Uri?) : AddBookIntent
}