package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.base.ext.orEmpty
import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.model.TextStyle
import org.jsoup.nodes.Element
import javax.inject.Inject

class ElementMapper @Inject constructor() {

    fun map(
        element: Element?,
        binaries: Map<String, ByteArray>,
        styles: Set<TextStyle> = emptySet(),
    ): List<ElementUI> {
        if (element == null) return emptyList()

        return when (element.tagName()) {
            "image" -> image(element, binaries).orEmpty()
            "empty-line" -> listOf(ElementUI.EmptyLineUI)
            else -> complexComponent(element, binaries, styles)
        }
    }

    private fun complexComponent(
        element: Element,
        binaries: Map<String, ByteArray>,
        styles: Set<TextStyle>,
    ): List<ElementUI> {
        val result = mutableListOf<ElementUI>()
        val children = element.childNodes()
            .flatMap { node ->
                /*
                todo:
                тут я планирую прокидывать стили до самого дна
                как-то мне нужно определять что текущая нода - полностью текстовая, которую нужно подать цельным блоком
                и сделать ей отдельную ветку с TextUI(parts = разные стили текстов, склееные в будущем в один компонент)
                в перспективе это должно сработать и с poem,
                т.к. у меня на компоненте будет стили: Poem, Title, естесственно я сделаю Заголовок стиха,
                а когда Poem, Line я сделаю строчку стиха
                то же со всякими таблицами и тд, я могу на пост-продакшне склеивать все соседние Table. Xxx например теги в одну таблицу
                мне кажется что это очень хороший будет подход,

                не понимаю только пока что сделать в when чтобы достоверно распознать нижний текстовый уровень и применить ему стили,
                будет ли он рекурсивничать? или лучше сделать ему отдельное ответвление?
                 */
            }

        return children
    }

    private fun image(
        element: Element,
        binaries: Map<String, ByteArray>,
    ): ElementUI? {
        val KEY = "xlink:href"
        val SHARP = "#"
        val ref = element.attr(KEY).removePrefix(SHARP)
        val image = binaries[ref]
        return image?.let { ElementUI.ImageUI(it) }
    }
}