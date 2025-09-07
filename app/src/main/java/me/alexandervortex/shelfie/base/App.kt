package me.alexandervortex.shelfie.base

import android.app.Application
import android.net.Uri
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {

        var uri: Uri? = null
    }
}