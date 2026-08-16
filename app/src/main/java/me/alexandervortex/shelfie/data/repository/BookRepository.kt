package me.alexandervortex.shelfie.data.repository

import android.net.Uri
import me.alexandervortex.shelfie.data.db.BookDao
import me.alexandervortex.shelfie.data.parser.UniversalFileParser
import me.alexandervortex.shelfie.model.PreviewBookModel
import me.alexandervortex.shelfie.ui.model.BookUIModel
import javax.inject.Inject

@Deprecated("need to be splitted on feature repositories")
class BookRepository
@Inject constructor(
    private val dao: BookDao,
    private val parser: UniversalFileParser,
) {

    fun previewFromUri(uri: Uri): PreviewBookModel? {
        return parser.previewFromUri(uri)
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
}
