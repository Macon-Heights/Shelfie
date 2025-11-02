package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.base.ext.getBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object ElementMapperXmlTestData {

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

    fun paragraphWithTextAndImage() = """
        <body>
            <p>
                Перед картинкой — текст.
                <image l:href="#img01.jpg"/>
                После картинки — текст.
            </p>
        </body>
    """.trimIndent()

    fun nestedStyles() = """
        <body>
            <p>
                Это <strong>жирный и <i>курсив внутри жирного</i></strong>, а это <u>подчёркнутый</u>.
            </p>
        </body>
    """.trimIndent()

    fun emptyParagraph() = """
        <body>
            <p>   </p>
        </body>
    """.trimIndent()
}