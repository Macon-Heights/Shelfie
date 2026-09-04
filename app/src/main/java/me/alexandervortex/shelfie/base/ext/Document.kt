package me.alexandervortex.shelfie.base.ext

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

private const val BODY = "FictionBook > body"
private const val TITLE_INFO = "description > title-info"
private const val BINARY = "binary"
private const val ID = "id"

fun Document.getBody(): Element? {
    return selectFirst(BODY) ?: selectFirst("body")
}

fun Document.getTitleInfo(): Element? {
    return selectFirst(TITLE_INFO) ?: selectFirst("title-info")
}

suspend fun Document.getBinaries(): Map<String, ByteArray> = coroutineScope {
    select(BINARY)
        .map { element ->
            async(Dispatchers.Default) {
                val binaryId = element.attr(ID)
                val base64 = element.text().trim()
                binaryId to Base64.decode(base64, Base64.DEFAULT)
            }
        }
        .awaitAll()
        .toMap()
}
