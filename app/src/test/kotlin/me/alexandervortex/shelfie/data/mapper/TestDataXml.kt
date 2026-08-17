package me.alexandervortex.shelfie.data.mapper

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object TestDataXml {


    fun String?.toElement(): Element? {
        return this?.let { Jsoup.parse(it.trimIndent()) }
    }

    fun binaries(): Map<String, ByteArray> {
        return mapOf(
            "_1.jpg" to "_1.jpg".toByteArray(),
            "_2.jpg" to "_2.jpg".toByteArray(),
            "_3.jpg" to "_3.jpg".toByteArray()
        )
    }
}