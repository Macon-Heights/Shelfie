package me.alexandervortex.shelfie.feature.updater

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubAssetDto(
    val name: String,

    @SerialName("browser_download_url")
    val downloadUrl: String,

    val size: Long,
)