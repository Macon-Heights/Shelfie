package me.alexandervortex.shelfie.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.alexandervortex.shelfie.data.datasource.Fb2DataSource
import me.alexandervortex.shelfie.data.db.BookDb
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.mapper.BookEntityMapper
import me.alexandervortex.shelfie.data.repository.RepositoryImpl
import javax.inject.Singleton

const val BOOK_DB = "book_db"

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideDataSource(
        @ApplicationContext context: Context,
    ): Fb2DataSource {
        return Fb2DataSource(context)
    }

    @Provides
    @Singleton
    fun provideRepo(
        dao: BookDao,
        mapper: BookEntityMapper,
        dataSource: Fb2DataSource,
    ): RepositoryImpl {
        return RepositoryImpl(dao, mapper, dataSource)
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