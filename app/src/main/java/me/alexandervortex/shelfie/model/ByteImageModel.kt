package me.alexandervortex.shelfie.model

interface ImageModel

data class ByteImageModel(
    val image: ByteArray? = null
) : ImageModel {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ByteImageModel

        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        return image?.contentHashCode() ?: 0
    }
}