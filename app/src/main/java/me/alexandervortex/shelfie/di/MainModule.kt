package me.alexandervortex.shelfie.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.alexandervortex.shelfie.data.db.BookDao
import me.alexandervortex.shelfie.data.db.BookDatabase
import me.alexandervortex.shelfie.feature.updater.GitHubApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

const val BOOK_DB = "book_db"

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideGitHubApi(
        json: Json,
    ): GitHubApi {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
            .create(GitHubApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookDatabase(
        @ApplicationContext context: Context,
    ): BookDatabase {
        return Room.databaseBuilder(
            context,
            BookDatabase::class.java,
            BOOK_DB
        ).addMigrations(BookDatabase.MIGRATION_2_3)
            .build()
    }

    @Provides
    fun provideBookDao(
        database: BookDatabase,
    ): BookDao {
        return database.bookDao()
    }
}