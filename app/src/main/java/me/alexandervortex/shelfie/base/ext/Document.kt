package me.alexandervortex.shelfie.base.ext

import android.util.Base64
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

private const val BODY = "body"
private const val TITLE_INFO = "description > title-info"
private const val BINARY = "binary"
private const val ID = "id"

fun Document.getBody(): Element? {
    return selectFirst(BODY)
}

fun Document.getTitleInfo(): Element? {
    return selectFirst(TITLE_INFO)
}

fun Document.getBinaries(): Map<String, ByteArray> {
    return select(BINARY)
        .associate {
            val binaryId = it.attr(ID)
            val base64 = it.text().trim()
            binaryId to Base64.decode(base64, Base64.DEFAULT)
        }
}
