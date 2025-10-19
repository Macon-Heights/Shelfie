package me.alexandervortex.shelfie.features.viewer

import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.model.TitleInfoUI

fun getBookUI(): BookUI {
    return BookUI(
        titleInfo = getTitleInfo(),
        elements = getElements(),
        progressIndex = 0,
        progressOffset = 0
    )
}

private fun getTitleInfo(): TitleInfoUI {
    return TitleInfoUI(
        id = "mock_book",
        localPath = "fake_path",
        title = "Mock Book",
        date = "2025",
        author = "Sashke Vortex",
        annotation = "Mock Book\nThis is just description for a book. This is just description for a book.\n\nby Sashke Vortex",
        genre = "Horror",
        lang = "en",
        coverImage = null
    )
}

private fun getElements(): List<ElementUI> {
    return listOf(

        ElementUI.TextUI(
            parts = listOf(
                "1. Солнце встаёт над рекой голубой,\n",
                "2. Ветер играет листвою живой,\n",
                "3. Птицы поют, засыпая туман,\n",
                "4. День обещает быть добрым нам.\n"
            )
        ),

        ElementUI.TextUI(
            parts = listOf(
                "1. Вечер ложится на крыши домов,\n",
                "2. Город гудит, но становится ров,\n",
                "3. Месяц качается в облаках,\n",
                "4. Свет отражается в наших глазах.\n"
            )
        ),

        ElementUI.TextUI(
            parts = listOf(
                "1. Мысли плывут, как корабли,\n",
                "2. Сердце стремится за ними вдали,\n",
                "3. Пусть будет путь наш светел и прост,\n",
                "4. В каждом дыханьи — надежды мост.\n"
            )
        ),

        ElementUI.TextUI(
            parts = listOf(
                "1. Солнце встаёт над рекой голубой,\n",
                "2. Ветер играет листвою живой,\n",
                "3. Птицы поют, засыпая туман,\n",
                "4. День обещает быть добрым нам.\n"
            )
        ),

        ElementUI.TextUI(
            parts = listOf(
                "1. Вечер ложится на крыши домов,\n",
                "2. Город гудит, но становится ров,\n",
                "3. Месяц качается в облаках,\n",
                "4. Свет отражается в наших глазах.\n"
            )
        ),

        ElementUI.TextUI(
            parts = listOf(
                "1. Мысли плывут, как корабли,\n",
                "2. Сердце стремится за ними вдали,\n",
                "3. Пусть будет путь наш светел и прост,\n",
                "4. В каждом дыханьи — надежды мост.\n"
            )
        ),

        ElementUI.TextUI(
            parts = listOf(
                "1. Солнце встаёт над рекой голубой,\n",
                "2. Ветер играет листвою живой,\n",
                "3. Птицы поют, засыпая туман,\n",
                "4. День обещает быть добрым нам.\n"
            )
        ),

        ElementUI.TextUI(
            parts = listOf(
                "1. Вечер ложится на крыши домов,\n",
                "2. Город гудит, но становится ров,\n",
                "3. Месяц качается в облаках,\n",
                "4. Свет отражается в наших глазах.\n"
            )
        ),

        ElementUI.TextUI(
            parts = listOf(
                "1. Мысли плывут, как корабли,\n",
                "2. Сердце стремится за ними вдали,\n",
                "3. Пусть будет путь наш светел и прост,\n",
                "4. В каждом дыханьи — надежды мост.\n"
            )
        ),

        ElementUI.TextUI(
            parts = listOf(
                "1. Солнце встаёт над рекой голубой,\n",
                "2. Ветер играет листвою живой,\n",
                "3. Птицы поют, засыпая туман,\n",
                "4. День обещает быть добрым нам.\n"
            )
        ),

        ElementUI.TextUI(
            parts = listOf(
                "1. Вечер ложится на крыши домов,\n",
                "2. Город гудит, но становится ров,\n",
                "3. Месяц качается в облаках,\n",
                "4. Свет отражается в наших глазах.\n"
            )
        ),

        ElementUI.TextUI(
            parts = listOf(
                "1. Мысли плывут, как корабли,\n",
                "2. Сердце стремится за ними вдали,\n",
                "3. Пусть будет путь наш светел и прост,\n",
                "4. В каждом дыханьи — надежды мост.\n"
            )
        )
    )
}