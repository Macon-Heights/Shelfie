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
            "_1.jpg" to "_1.jpg".toByteArray(),
            "_2.jpg" to "_2.jpg".toByteArray(),
            "_3.jpg" to "_3.jpg".toByteArray()
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