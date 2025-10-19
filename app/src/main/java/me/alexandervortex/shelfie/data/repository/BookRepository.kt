package me.alexandervortex.shelfie.data.repository

import android.net.Uri
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.data.db.mapper.BookEntityMapper
import me.alexandervortex.shelfie.data.parser.UniversalFileParser
import me.alexandervortex.shelfie.features.viewer.getBookUI
import me.alexandervortex.shelfie.ui.model.BookUI
import javax.inject.Inject

/**
 * Репозиторий книжек
 * добавляет книжку
 * получает список книжек
 * получает книгу по айди
 */

private const val FAKE_BOOK_ID = "fake_book_id"

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

        entity?.let {
            dao.insert(it)
        }
    }

    /**
     * получаем список всех наших книг ( в папке и в бд)
     * справедливо для любого типа книги
     */
    suspend fun getBookEntities(): List<BookEntity> {
        val entities = dao.getBookEntities().toMutableList()
        entities.add(getFakeBook())
        return entities
    }

    private fun getFakeBook(): BookEntity {
        return BookEntity(
            id = FAKE_BOOK_ID,
            localPath = "fake_path",
            title = "Mock Book",
            author = "Sashke Vortex",
            year = "2025",
            scrollOffset = 0,
            scrollIndex = 0
        )
    }

    /**
     * получаем книгу по айди из бд + сам файл из папки
     * справедливо для любого типа книги
     */
    suspend fun getBookModelById(id: String): BookUI? {
        if (id == FAKE_BOOK_ID) return getBookUI()

        val entity = dao.getById(id)
        val result = entity?.let {
            parser.getBookModelById(
                id = entity.id,
                localPath = entity.localPath,
                scrollOffset = entity.scrollOffset,
                scrollIndex = entity.scrollIndex
            )
        }
        return result
    }

    suspend fun saveCurrentBookProgress(
        id: String,
        index: Int,
        offset: Int,
    ) {
        dao.updateProgress(id, index, offset)
    }
}
