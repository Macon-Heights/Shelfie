package me.alexandervortex.shelfie.feature.updater

import retrofit2.http.GET

interface GitHubApi {

    @GET("repos/Macon-Heights/Shelfie/releases/latest")
    suspend fun getLatestRelease(): GitHubReleaseDto
}