package me.alexandervortex.shelfie.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.alexandervortex.shelfie.data.db.BookDatabase
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.mapper.BookEntityMapper
import me.alexandervortex.shelfie.data.parser.FictionBookParser
import me.alexandervortex.shelfie.data.parser.UniversalFileParser
import me.alexandervortex.shelfie.data.repository.BookRepository
import javax.inject.Singleton

const val BOOK_DB = "book_db"

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideFictionBookParser(
        @ApplicationContext context: Context,
        mapper: FictionBookParser,
    ): UniversalFileParser {
        return UniversalFileParser(context, mapper)
    }

    @Provides
    @Singleton
    fun provideBookRepository(
        dao: BookDao,
        mapper: BookEntityMapper,
        parser: UniversalFileParser,
    ): BookRepository {
        return BookRepository(dao, mapper, parser)
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