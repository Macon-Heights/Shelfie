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

    fun paragraphWithFewTexts() = """
        <body>
            <p>
                He apiti atu ki enei, kaua e meinga hei ritenga wehewehe te mea i whakakaupapatia na runga i nga whakahaere ture, i nga mana whanui ranei o te ao kua whakawhiwhia ki tetahi whenua ki tetahi wahanga whenua ranei no reira nei tetahi tangata, ahakoa taua wahanga whenua he whai mana motuhake, kei raro ranei i te Kaitiakitanga, he takiwa whenua ranei kahore nei ona Mana Kawanatanga Motuhake, kei raro ranei i tetahi atu ritenga whakawhaiti i tona mana motuhake.
                <strong>Rarangi 3</strong>
            </p>
        </body>
    """.trimIndent().toBody()
}