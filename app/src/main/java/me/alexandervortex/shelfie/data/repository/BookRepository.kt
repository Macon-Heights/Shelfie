package me.alexandervortex.shelfie.data.repository

import android.net.Uri
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.db.entity.BookEntity
import me.alexandervortex.shelfie.data.mapper.BookEntityMapper
import me.alexandervortex.shelfie.data.parser.UniversalFileParser
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.CatalogueItemUIModel
import me.alexandervortex.shelfie.ui.model.TitleInfoUIModel
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

    fun previewFromUri(uri: Uri): TitleInfoUIModel? {
        return parser.previewFromUri(uri)
    }

    // just take book and place it to db, return NOTHING
    suspend fun importFromUri(uri: Uri) {
        /**
         * uri -> universal parser -> book model
         * эта часть понятна и логична и пусть остается так
         */
        val bookModel = parser.importFromUri(uri)
            ?: throw Exception("Книга не поддерживается.\nТолько FB2 формат.")

        /**
         * book model -> database
         * это справедливо для любой книги в любом формате
         */
        val entity = bookModel.let {
            mapper.toEntity(it)
        }

        entity.let {
            dao.insert(it)
        }
    }

    /**
     * получаем список всех наших книг ( в папке и в бд)
     * справедливо для любого типа книги
     */
    suspend fun getBookEntities(): List<BookEntity> {
        val entities = dao.getBookEntities().toMutableList()
        return entities
    }

    /**
     * получаем книгу по айди из бд + сам файл из папки
     * справедливо для любого типа книги
     */
    suspend fun getBookModelById(id: String): BookUIModel? {

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
        elements: Int,
    ) {
        dao.updateProgress(id, index, offset, elements)
    }

    suspend fun removeChecked(books: List<CatalogueItemUIModel.Model>) {
        dao.removeBooks(books.map { it.id })
        parser.removeBooksByPath(books.map { it.localPath })
    }
}
