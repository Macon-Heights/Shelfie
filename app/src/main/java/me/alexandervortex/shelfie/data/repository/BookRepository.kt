package me.alexandervortex.shelfie.data.repository

import android.net.Uri
import me.alexandervortex.shelfie.base.Lg
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.data.db.mapper.BookEntityMapper
import me.alexandervortex.shelfie.data.parser.UniversalFileParser
import me.alexandervortex.shelfie.ui.model.BookUI
import javax.inject.Inject

/**
 * Репозиторий книжек
 * добавляет книжку
 * получает список книжек
 * получает книгу по айди
 */
class BookRepository
@Inject constructor(
    private val dao: BookDao,
    private val mapper: BookEntityMapper,
    private val parser: UniversalFileParser,
) {

    private val lg = Lg("BookRepository")

    suspend fun importFromUri(uri: Uri) {
        lg.log("importFromUri start")
        /**
         * uri -> universal parser -> book model
         * эта часть понятна и логична и пусть остается так
         */
        val bookModel = parser.importFromUri(uri)

        /**
         * book model -> database
         * это справедливо для любой книги в любом формате
         */
        val entity = bookModel?.let {
            mapper.toEntity(it)
        }

        entity?.let {
            dao.insert(it)
            lg.log("importFromUri end")
        }
    }

    /**
     * получаем список всех наших книг ( в папке и в бд)
     * справедливо для любого типа книги
     */
    suspend fun getBookEntities(): List<BookEntity> {
        lg.log("getBookEntities start")
        val entities = dao.getBookEntities()
        lg.log("getBookEntities end")
        return entities
    }

    /**
     * получаем книгу по айди из бд + сам файл из папки
     * справедливо для любого типа книги
     */
    suspend fun getBookModelById(id: String): BookUI? {
        lg.log("getBookModelById start")
        val entity = dao.getById(id)
        val result = entity?.let {
            parser.getBookModelById(entity.id, entity.localPath)
        }
        lg.log("getBookModelById end")
        return result
    }
}
