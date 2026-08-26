package me.alexandervortex.shelfie.feature.viewer

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import me.alexandervortex.shelfie.base.transformerTest
import me.alexandervortex.shelfie.data.mapper.ElementMapperTestData.paragraphComplexEpubModel
import me.alexandervortex.shelfie.data.mapper.ElementMapperTestData.paragraphComplexModel
import me.alexandervortex.shelfie.data.mapper.ElementMapperTestData.paragraphEmptyModel
import me.alexandervortex.shelfie.data.mapper.ElementMapperTestData.paragraphWithTextModel
import me.alexandervortex.shelfie.feature.viewer.ViewerUIFactoryTestData.uiComplex
import me.alexandervortex.shelfie.feature.viewer.ViewerUIFactoryTestData.uiComplexEpub
import me.alexandervortex.shelfie.feature.viewer.ViewerUIFactoryTestData.uiEmpty
import me.alexandervortex.shelfie.feature.viewer.ViewerUIFactoryTestData.uiWithText
import me.alexandervortex.shelfie.model.BookDocument
import me.alexandervortex.shelfie.model.ParsedBookModel
import me.alexandervortex.shelfie.model.ProgressBookModel
import me.alexandervortex.shelfie.model.ProgressModel
import me.alexandervortex.shelfie.ui.model.BookUIModel

class ViewerUIFactoryTest : BehaviorSpec({
    val factory = ViewerUIFactory()

    forAll(
        table(
            headers("ui", "model", "name"),
            row(uiEmpty(), paragraphEmptyModel().toProgressBookModel(), "paragraph empty"),
            row(uiWithText(), paragraphWithTextModel().toProgressBookModel(), "paragraph with text"),
            row(uiComplex(), paragraphComplexModel().toProgressBookModel(), "paragraph complex"),
//            row(uiComplexEpub(), paragraphComplexEpubModel().toProgressBookModel(), "paragraph complex epub"),
        )
    ) { ui: BookUIModel?, model: ProgressBookModel, name: String ->

        transformerTest(ui, name) {
            factory.getBookUIModel(model)
        }
    }
})

private fun BookDocument.toProgressBookModel() = ProgressBookModel(
    id = "id",
    localPath = "path",
    progress = ProgressModel(),
    book = ParsedBookModel(
        titleInfo = getTitleInfo(),
        document = this
    )
)
