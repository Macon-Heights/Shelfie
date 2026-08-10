package me.alexandervortex.shelfie.ui.model

data class TitleInfoUIModel(
    val title: String?,
    val date: String?,
    val author: String?,
    val annotation: String?,
    val genre: String?,
    val lang: String?,
    val coverImage: ByteArray?,
    val manyImages: List<ByteArray?>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TitleInfoUIModel

        if (title != other.title) return false
        if (date != other.date) return false
        if (author != other.author) return false
        if (annotation != other.annotation) return false
        if (genre != other.genre) return false
        if (lang != other.lang) return false
        if (!coverImage.contentEquals(other.coverImage)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title?.hashCode() ?: 0
        result = 31 * result + (date?.hashCode() ?: 0)
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + (annotation?.hashCode() ?: 0)
        result = 31 * result + (genre?.hashCode() ?: 0)
        result = 31 * result + (lang?.hashCode() ?: 0)
        result = 31 * result + (coverImage?.contentHashCode() ?: 0)
        return result
    }
}