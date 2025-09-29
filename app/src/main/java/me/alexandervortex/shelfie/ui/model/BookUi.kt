package me.alexandervortex.shelfie.ui.model

// Книга целиком
data class BookUi(
    // for db and files
    val id: String,
    val localPath: String,

    // for grid view
    val title: String,
    val year: String?,
    val author: String?,
    val annotation: String?,
    val coverImage: ByteArray?,

    // book body
    val sections: List<SectionUi>,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BookUi

        if (id != other.id) return false
        if (localPath != other.localPath) return false
        if (title != other.title) return false
        if (year != other.year) return false
        if (author != other.author) return false
        if (annotation != other.annotation) return false
        if (coverImage != null) {
            if (other.coverImage == null) return false
            if (!coverImage.contentEquals(other.coverImage)) return false
        } else if (other.coverImage != null) return false
        if (sections != other.sections) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + localPath.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (year?.hashCode() ?: 0)
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + (annotation?.hashCode() ?: 0)
        result = 31 * result + (coverImage?.contentHashCode() ?: 0)
        result = 31 * result + sections.hashCode()
        return result
    }
}