package me.alexandervortex.shelfie.data.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.alexandervortex.shelfie.base.ext.toModel
import me.alexandervortex.shelfie.data.db.BookDao
import me.alexandervortex.shelfie.data.mapper.BookEntityMapper
import me.alexandervortex.shelfie.data.parser.UniversalFileParser
import me.alexandervortex.shelfie.model.PreviewBookModel
import me.alexandervortex.shelfie.model.CatalogueItemModel
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.CatalogueItemUIModel
import javax.inject.Inject

class BookRepository
@Inject constructor(
    private val dao: BookDao,
    private val mapper: BookEntityMapper,
    private val parser: UniversalFileParser,
) {

    fun previewFromUri(uri: Uri): PreviewBookModel? {
        return parser.previewFromUri(uri)
    }

    suspend fun addBookToDbAndDisk(uri: Uri) {
        val model = parser.parseAndCopy(uri) ?: throw Exception("Book not supported")
        val entity = mapper.toEntity(model)
        dao.addBook(entity)
    }

    fun getPreviews(): Flow<List<CatalogueItemModel>> {
        return dao.getPreviews().map { entities ->
            entities.map { it.toModel() }
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
        dao.removeBooks(books.map { it.data.id })
        parser.removeBooks(books.map { it.data.localPath })
    }
}
