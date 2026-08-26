package me.alexandervortex.shelfie.feature.viewer

import me.alexandervortex.shelfie.model.PreviewBookModel
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.StyledText
import me.alexandervortex.shelfie.ui.model.TextStyleUIModel
import me.alexandervortex.shelfie.ui.model.UI
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object ViewerUIFactoryTestData {

    fun String?.toElement(): Element? {
        return this?.let { Jsoup.parse(it.trimIndent()) }
    }

    fun uiComplexEpub(): BookUIModel {
        return BookUIModel(
            id = "id", localPath = "path",
            titleInfo = PreviewBookModel(
                title = "Frankenstein",
                date = "1818",
                author = "Mary Shelley",
                annotation = "Victor Frankenstein, a young scientist driven by ambition, discovers a way to create life. However, he is horrified by the creature he brings into the world and abandons it. Rejected by society and desperate for companionship, the creature turns against its creator. The novel explores responsibility, loneliness, prejudice, and the consequences of uncontrolled ambition.",
                genre = "Gothic Fiction",
                lang = "en",
                coverImage = null,
                gallery = emptyList()
            ),
            elements = listOf(
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Ночь на мосту"
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "По мотивам народных поверий"
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Это тот старикан, который раньше каждый год приезжал на ярмарку в В."
                        ), StyledText(
                            styles = setOf(
                                TextStyleUIModel.Link("ch2.xhtml#id49")
                            ), text = "[1]"
                        ),
                        StyledText(
                            styles = emptySet(),
                            text = ", привозил на продажу калган-траву и горечавку. Под конец заговорили о вампирах, здухачах"
                        ), StyledText(
                            styles = setOf(
                                TextStyleUIModel.Link("ch2.xhtml#id48")
                            ), text = "[2]"
                        ),
                        StyledText(
                            styles = emptySet(),
                            text = ", джиннах, ведьмах и о другой нечистой силе, какая только может в глухую ночь человеку явиться."
                        )
                    )
                )
            ),
            progressIndex = 0,
            progressOffset = 0
        )
    }

    fun uiComplex(): BookUIModel {
        return BookUIModel(
            id = "id", localPath = "path", titleInfo = PreviewBookModel(
                title = "Frankenstein",
                date = "1818",
                author = "Mary Shelley",
                annotation = "Victor Frankenstein, a young scientist driven by ambition, discovers a way to create life. However, he is horrified by the creature he brings into the world and abandons it. Rejected by society and desperate for companionship, the creature turns against its creator. The novel explores responsibility, loneliness, prejudice, and the consequences of uncontrolled ambition.",
                genre = "Gothic Fiction",
                lang = "en",
                coverImage = null,
                gallery = emptyList()
            ), elements = listOf(
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = setOf(TextStyleUIModel.Bold),
                            text = "Введение"
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Летом 1960 года я, в ту пору студент факультета антропологии при Калифорнийском университете в Лос-Анжелесе, предпринял несколько поездок на Юго-Запад с целью сбора информации о лекарственных растениях используемых местными индейцами. К одной из этих поездок относится начало описываемых здесь событий."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Я ожидал автобуса на станции в приграничном городишке, болтая с приятелем, который сопровождал меня в качестве гида и помощника. Вдруг он наклонился ко мне и прошептал, что вон тот старый седой индеец, который сидит у окна, здорово разбирается в растениях, а в пейоте особенно. Я попросил нас познакомить."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Приятель окликнул старика, потом подошел к нему и пожал руку. Поговорив с минуту, он жестом подозвал меня и исчез, предоставив мне самому выпутываться из положения. Старик остался невозмутимым. Я представился; он сказал, что зовут его Хуан и что он к моим услугам. По-испански это было сказано с отменной учтивостью. Мы обменялись по моей инициативе рукопожатием и оба замолчали. Это молчание, однако, нельзя было назвать натянутым, оно было спокойным и естественным."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Хотя морщины, покрывавшие его смуглое лицо и шею, свидетельствовали о почтенном возрасте, меня поразило его тело — поджарое и мускулистое. Я сообщил ему, что собираю сведения о лекарственных растениях. По совести, я почти ничего не знал о пейоте, однако получилось так, будто я дал понять, что в пейоте я просто дока и что ему вообще стоит сойтись со мной поближе."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Пока я нес эту ахинею, он медленно кивнул и взглянул на меня, не говоря ни слова. Я невольно отвел глаза, и сцена закончилась гробовым молчанием. Наконец, после нестерпимо затянувшейся паузы, дон Хуан поднялся и выглянул в окно. Подошел его автобус. Он попрощался и уехал."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Я был раздражен своей дурацкой болтовней под его необычным взглядом, который, казалось, читал меня насквозь."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Вернувшийся приятель, узнав о моей неудачной попытке выведать что-нибудь от дона Хуана, постарался меня утешить, — старик, мол, вообще неразговорчив и замкнут. Однако тягостное впечатление от этой первой встречи было не так-то легко рассеять."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Я приложил усилия, чтобы разузнать, где живет дон Хуан, и после не раз приезжал к нему в гости. При каждой встрече я пытался навести разговор на тему пейота, но безуспешно. Мы, тем не менее, стали хорошими друзьями, и со временем мои научные изыскания были позабыты или, во всяком случае, приобрели совершенно новое направление, о котором я вначале не мог и подозревать."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Приятель, который нас познакомил, после разъяснил, что дон Хуан не был уроженцем Аризоны, где мы встретились: он родился в мексиканском штате Сонора, в племени индейцев яки."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Поначалу дон Хуан был для меня попросту занятным стариком, который очень хорошо говорит по-испански и превосходно разбирается в пейоте. Однако знавшие его утверждали, что он «брухо» — целитель, знахарь, колдун, маг."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Прошел целый год, прежде чем он начал мне доверять. В один прекрасный день он сообщил, что обладает особыми знаниями, которые передал ему «бенефактор», — так он называл своего учителя. Теперь дон Хуан, в свою очередь, избрал меня своим учеником и предупредил, что мне предстоит сделать очень серьезный выбор, так как обучение будет долгим и трудным."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "В системе представлений дона Хуана процесс приобретения «союзника» означал главным образом использование состояний необычной реальности, которые он во мне вызывал с помощью галлюциногенных растений. Он считал, что, фокусируя внимание на этих состояниях и подчиняя этому прочие аспекты знания, которое я от него получал, я приду к адекватному восприятию магической реальности."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Книга, таким образом, содержит наиболее важные фрагменты моих полевых записей, где речь идет об испытываемых мною в процессе обучения состояниях необычной реальности. Порядок подачи фрагментов не всегда хронологический, поскольку я следовал логике развертывания учения. Я никогда не записывал свои впечатления прежде, чем они улягутся и я смогу осмыслить их сравнительно беспристрастно. Однако комментарии дона Хуана к испытанному мною в очередной раз я записывал немедленно, поэтому подчас они опережают описание самого опыта."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Мои полевые записи представляют субъективную интерпретацию того, что я испытывал непосредственно во время опыта. Эта интерпретация воспроизводится здесь в точном соответствии с моим изложением испытанного дону Хуану, который требовал исчерпывающего и точного воспроизведения каждой детали и подробнейшего пересказа каждого переживания."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "При записи я добавлял для полноты картины некоторые бытовые детали. Кроме того, в записках содержатся также попытки толкования мировоззрения дона Хуана."
                        )
                    )
                ),
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "Чтобы избежать повторений, я упростил наши диалоги и убрал все второстепенное. Однако, чтобы передать все же общую атмосферу, мои правки коснулись лишь тех диалогов, в которых не содержалось ничего нового, что способствовало бы моему постижению этого пути. Информация от дона Хуана всегда была спорадической, и подчас малейшее его замечание вызывало целую лавину расспросов, которые длились часами. С другой стороны, было множество случаев, когда он все рассказывал сам."
                        )
                    )
                )
            ),
            progressIndex = 0,
            progressOffset = 0
        )
    }

    fun uiEmpty(): BookUIModel {
        return BookUIModel(
            id = "id", localPath = "path", titleInfo = PreviewBookModel(
                title = "Frankenstein",
                date = "1818",
                author = "Mary Shelley",
                annotation = "Victor Frankenstein, a young scientist driven by ambition, discovers a way to create life. However, he is horrified by the creature he brings into the world and abandons it. Rejected by society and desperate for companionship, the creature turns against its creator. The novel explores responsibility, loneliness, prejudice, and the consequences of uncontrolled ambition.",
                genre = "Gothic Fiction",
                lang = "en",
                coverImage = null,
                gallery = emptyList()
            ), elements = emptyList(),
            progressIndex = 0,
            progressOffset = 0
        )
    }

    fun uiWithText(): BookUIModel? {
        return BookUIModel(
            id = "id",
            localPath = "path",
            titleInfo = PreviewBookModel(
                title = "Frankenstein",
                date = "1818",
                author = "Mary Shelley",
                annotation = "Victor Frankenstein, a young scientist driven by ambition, discovers a way to create life. However, he is horrified by the creature he brings into the world and abandons it. Rejected by society and desperate for companionship, the creature turns against its creator. The novel explores responsibility, loneliness, prejudice, and the consequences of uncontrolled ambition.",
                genre = "Gothic Fiction",
                lang = "en",
                coverImage = null,
                gallery = emptyList()
            ),
            elements = listOf(
                UI.ComplexText(
                    parts = listOf(
                        StyledText(
                            styles = emptySet(),
                            text = "sashka have keked 3 times"
                        )
                    )
                )
            ),
            progressIndex = 0,
            progressOffset = 0
        )
    }
}