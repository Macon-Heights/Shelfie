package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.base.ext.getBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object TestDataXml {

    fun String?.toBody(): Element? {
        return this?.let { Jsoup.parse(it).getBody() }
    }

    fun binaries(): Map<String, ByteArray> {
        return mapOf(
            "img1.jpg" to "img1".toByteArray(),
            "img2.jpg" to "img2".toByteArray(),
            "img3.jpg" to "img3".toByteArray()
        )
    }

    fun paragraphEmpty() = """
        <body>
            <p>   </p>
        </body>
    """.trimIndent().toBody()

    fun paragraphWithText() = """
        <body>
            <p>sashka have keked 3 times</p>
        </body>
    """.trimIndent().toBody()
}