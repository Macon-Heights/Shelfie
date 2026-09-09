package me.alexandervortex.shelfie.feature.updater

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubReleaseDto(
    @SerialName("tag_name")
    val tagName: String,

    val name: String? = null,

    val body: String? = null,

    val assets: List<GitHubAssetDto> = emptyList(),
)