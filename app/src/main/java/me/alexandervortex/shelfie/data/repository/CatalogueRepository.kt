package me.alexandervortex.shelfie.data.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.alexandervortex.shelfie.data.db.BookDao
import me.alexandervortex.shelfie.data.mapper.BookEntityMapper
import me.alexandervortex.shelfie.data.parser.FileHelper
import me.alexandervortex.shelfie.data.parser.UniversalFileParser
import me.alexandervortex.shelfie.data.parser.ZipHelper
import me.alexandervortex.shelfie.model.CatalogueItemModel
import me.alexandervortex.shelfie.model.PreviewBookModel
import me.alexandervortex.shelfie.ui.model.BookUIModel
import javax.inject.Inject

private val supportedBooks = setOf("fb2", "epub", "txt", "pdf")

class CatalogueRepository
@Inject constructor(
    private val dao: BookDao,
    private val mapper: BookEntityMapper,
    private val parser: UniversalFileParser,
    private val fileHelper: FileHelper,
    private val zipHelper: ZipHelper
) {

    fun previewBook(uri: Uri): PreviewBookModel? {
        return zipHelper.processUriContent(uri, supportedBooks) { stream, extension ->
            parser.bookParser(stream, extension)?.titleInfo
        }
    }

    fun getCatalogueItems(): Flow<List<CatalogueItemModel>> {
        return dao.getCatalogueItems().map { entities ->
            entities.map { mapper.toModel(it) }
        }
    }

    suspend fun removeCatalogueItems(
        books: List<CatalogueItemModel>
    ) {
        dao.removeCatalogueItems(books.map { it.id })
        fileHelper.deleteFiles(books.map { it.localPath })
    }

    suspend fun addBook(uri: Uri) {
        zipHelper.processUriContent(uri, supportedBooks) { stream, extension ->
            val id = System.currentTimeMillis().toString()
            val file = fileHelper.saveBookFile(
                id = id,
                extension = extension,
                stream = stream,
            )

            val model = file.inputStream().use { savedStream ->
                parser.bookParser(savedStream, extension)
            } ?: throw Exception("Book not supported")

            mapper.initFirstEntity(
                id = id,
                localPath = file.path,
                model = model,
            )
        }?.let { entity ->
            dao.addBook(entity)
        }
    }

    suspend fun updateProgress(
        id: String,
        index: Int,
        offset: Int,
        elements: Int,
    ) {
        dao.updateProgress(id, index, offset, elements)
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
}