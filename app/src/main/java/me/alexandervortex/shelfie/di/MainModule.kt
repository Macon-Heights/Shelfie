package me.alexandervortex.shelfie.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.alexandervortex.shelfie.data.datasource.UniversalFileParser
import me.alexandervortex.shelfie.data.db.BookDb
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.mapper.BookEntityMapper
import me.alexandervortex.shelfie.data.mapper.FictionBookParser
import me.alexandervortex.shelfie.data.repository.Repository
import javax.inject.Singleton

const val BOOK_DB = "book_db"

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideDataSource(
        @ApplicationContext context: Context,
        mapper: FictionBookParser,
    ): UniversalFileParser {
        return UniversalFileParser(context, mapper)
    }

    @Provides
    @Singleton
    fun provideRepo(
        dao: BookDao,
        mapper: BookEntityMapper,
        dataSource: UniversalFileParser,
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