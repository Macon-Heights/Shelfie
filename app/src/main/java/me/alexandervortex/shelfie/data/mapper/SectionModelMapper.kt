package me.alexandervortex.shelfie.data.mapper

import com.kursx.parser.fb2.Section
import me.alexandervortex.shelfie.data.model.ElementModel
import me.alexandervortex.shelfie.data.model.SectionModel
import javax.inject.Inject

class SectionModelMapper
@Inject constructor() {

    fun map(section: Section): SectionModel {
        return SectionModel(
            elements = section.elements.map {
                "${it.text.replace("\n", " ")}\n\n\n"
            }
        )
    }
}
