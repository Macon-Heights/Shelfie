package me.alexandervortex.shelfie.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.entiry.BookEntity

@Database(entities = [BookEntity::class], version = 2)
abstract class BookDb : RoomDatabase() {

    abstract fun bookDao(): BookDao
}