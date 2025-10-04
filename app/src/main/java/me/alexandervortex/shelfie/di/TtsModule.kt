package me.alexandervortex.shelfie.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.alexandervortex.shelfie.features.tts.TtsController
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TtsModule {

    @Provides
    @Singleton
    fun provideTtsController(
        @ApplicationContext context: Context,
    ): TtsController {
        return TtsController(context)
    }
}
