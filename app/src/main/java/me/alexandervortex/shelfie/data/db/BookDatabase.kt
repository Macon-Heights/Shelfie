package me.alexandervortex.shelfie.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [BookEntity::class],
    version = 3,
    exportSchema = false
)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {

        private const val NEW_FIELD = "elements"
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE $BOOK_TABLE ADD COLUMN $NEW_FIELD INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}