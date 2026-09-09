package me.alexandervortex.shelfie.feature.updater

import me.alexandervortex.shelfie.BuildConfig
import javax.inject.Inject

class UpdateRepository
@Inject constructor(
    private val gitHubApi: GitHubApi,
) {

    suspend fun getAvailableUpdate(): AppUpdate? {
        return try {
            val release = gitHubApi.getLatestRelease()

            val remoteVersion = release.tagName
                .removePrefix("v")

            val currentVersion = BuildConfig.VERSION_NAME

            if (!isNewerVersion(remoteVersion, currentVersion)) {
                null
            } else {
                val apk = release.assets
                    .firstOrNull { asset ->
                        asset.name.endsWith(
                            ".apk",
                            ignoreCase = true,
                        )
                    }
                    ?: return null

                AppUpdate(
                    versionName = remoteVersion,
                    downloadUrl = apk.downloadUrl,
                    changelog = release.body,
                    size = apk.size,
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun isNewerVersion(
        remote: String,
        current: String,
    ): Boolean {

        val remoteParts = remote
            .split(".")
            .map { it.toIntOrNull() ?: 0 }

        val currentParts = current
            .split(".")
            .map { it.toIntOrNull() ?: 0 }

        val maxSize = maxOf(
            remoteParts.size,
            currentParts.size,
        )

        for (index in 0 until maxSize) {
            val remotePart =
                remoteParts.getOrElse(index) { 0 }

            val currentPart =
                currentParts.getOrElse(index) { 0 }

            if (remotePart > currentPart) return true
            if (remotePart < currentPart) return false
        }

        return true
    }
}