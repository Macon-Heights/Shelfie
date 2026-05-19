package me.alexandervortex.shelfie.data.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.alexandervortex.shelfie.base.ext.toBookComponentModel
import me.alexandervortex.shelfie.data.db.dao.BookDao
import me.alexandervortex.shelfie.data.mapper.BookEntityMapper
import me.alexandervortex.shelfie.data.parser.UniversalFileParser
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.CatalogueItemUIModel
import me.alexandervortex.shelfie.ui.model.TitleInfoUIModel
import javax.inject.Inject

class BookRepository
@Inject constructor(
    private val dao: BookDao,
    private val mapper: BookEntityMapper,
    private val parser: UniversalFileParser,
) {

    fun previewFromUri(uri: Uri): TitleInfoUIModel? {
        return parser.previewFromUri(uri)
    }

    suspend fun addBookToDbAndDisk(uri: Uri) {
        val model = parser.addBookToDbAndDisk(uri) ?: throw Exception("Книга не поддерживается.")
        val entity = mapper.toEntity(model)
        dao.addBook(entity)
    }

    fun getPreviews(): Flow<List<CatalogueItemUIModel.Model>> {
        return dao.getPreviews().map { entities ->
            entities.map { it.toBookComponentModel() }
        }
    }

    suspend fun getBookModelById(id: String): BookUIModel? {
        val entity = dao.getPreviewById(id)
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

    suspend fun updateProgress(
        id: String,
        index: Int,
        offset: Int,
        elements: Int,
    ) {
        dao.updateProgress(id, index, offset, elements)
    }

    suspend fun removeBooks(
        books: List<CatalogueItemUIModel.Model>
    ) {
        dao.removeBooks(books.map { it.id })
        parser.removeBooks(books.map { it.localPath })
    }
}
