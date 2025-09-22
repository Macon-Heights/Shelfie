package me.alexandervortex.shelfie.data.mapper

import com.kursx.parser.fb2.Element
import com.kursx.parser.fb2.FictionBook
import com.kursx.parser.fb2.Section
import me.alexandervortex.shelfie.data.model.FB2Model
import javax.inject.Inject

class FB2ModelMapper
@Inject constructor() {

    fun map(fb: FictionBook): FB2Model {
        val strings = fb.body.sections.flatMap { section: Section ->
            section.elements.map { element: Element? ->
                element?.text
            }
        }.filterNotNull()
        return FB2Model(strings)
    }
}