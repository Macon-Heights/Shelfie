package me.alexandervortex.shelfie.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.alexandervortex.shelfie.data.db.BookDb
import me.alexandervortex.shelfie.data.db.dao.BookDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BookDb {
        return Room.databaseBuilder(
            context, BookDb::class.java,"asda_asda"
        ).build()
    }

    @Provides
    fun provideDao(db:BookDb): BookDao {
        return db.bookDao()
    }

}