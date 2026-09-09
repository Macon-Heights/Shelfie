package me.alexandervortex.shelfie.feature.updater

data class AppUpdate(
    val versionName: String,
    val downloadUrl: String,
    val changelog: String?,
    val size: Long,
)