package me.alexandervortex.shelfie.features.addbook.mvi

import android.net.Uri

sealed interface AddBookIntent {
    data class Add(val uri: Uri) : AddBookIntent
}