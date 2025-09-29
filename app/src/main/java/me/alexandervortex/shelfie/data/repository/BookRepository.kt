package me.alexandervortex.shelfie.data.repository

import android.net.Uri
import me.alexandervortex.shelfie.data.parser.UniversalFileParser
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.data.db.mapper.BookEntityMapper
import me.alexandervortex.shelfie.data.model.BookUi
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

    suspend fun importFromUri(uri: Uri) {

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

        entity?.let { dao.insert(it) }
    }

    /**
     * получаем список всех наших книг ( в папке и в бд)
     * справедливо для любого типа книги
     */
    suspend fun getBookEntities(): List<BookEntity> {
        return dao.getBookEntities()
    }

    /**
     * получаем книгу по айди из бд + сам файл из папки
     * справедливо для любого типа книги
     */
    suspend fun getBookModelById(id: String): BookUi? {
        val entity = dao.getById(id)
        return entity?.let {
            parser.getBookModelById(entity.id, entity.localPath)
        }
    }
}
