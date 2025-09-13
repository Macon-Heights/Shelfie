package me.alexandervortex.shelfie.data.repository

import me.alexandervortex.shelfie.data.db.dao.BookDao
import javax.inject.Inject

class BookRepo
@Inject constructor(
    private val dao: BookDao,
) {


}