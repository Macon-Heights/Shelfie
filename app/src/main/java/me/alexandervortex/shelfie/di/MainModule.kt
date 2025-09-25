package me.alexandervortex.shelfie.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.alexandervortex.shelfie.data.datasource.FileSystemDataSource
import me.alexandervortex.shelfie.data.db.BookDb
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.mapper.BookEntityMapper
import me.alexandervortex.shelfie.data.mapper.BookModelMapper
import me.alexandervortex.shelfie.data.repository.Repository
import javax.inject.Singleton

const val BOOK_DB = "book_db"

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideDataSource(
        @ApplicationContext context: Context,
        mapper: BookModelMapper,
    ): FileSystemDataSource {
        return FileSystemDataSource(context, mapper)
    }

    @Provides
    @Singleton
    fun provideRepo(
        dao: BookDao,
        mapper: BookEntityMapper,
        dataSource: FileSystemDataSource,
    ): Repository {
        return Repository(dao, mapper, dataSource)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): BookDb {
        return Room.databaseBuilder(
            context,
            BookDb::class.java,
            BOOK_DB
        ).build()
    }

    @Provides
    fun provideDao(
        database: BookDb,
    ): BookDao {
        return database.bookDao()
    }
}