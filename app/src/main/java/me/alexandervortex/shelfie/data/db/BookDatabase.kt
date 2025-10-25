package me.alexandervortex.shelfie.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import me.alexandervortex.shelfie.data.db.dao.BOOK_TABLE
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.entiry.BookEntity

@Database(entities = [BookEntity::class], version = 2)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {

        private val NEW_FIELD = "elements"
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE $BOOK_TABLE ADD COLUMN $NEW_FIELD INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}