package me.alexandervortex.shelfie.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application()

/**
 * CHANGELOG:
 *
 * 1.0:
 * - Немного разделил классы
 * - Добавил UniversalParser (пока работающий по-старому)
 * - Все скомпилилось, я молодец
 *
 * 1.1
 * - Все парсится (хоть и не очень пока красиво, с игнором шрифтов)
 * - Работает сервис, в котором крутится ТТС
 * - Есть медиаплеер в шторке
 * - Работают кнопки таймер, скроость, некст, прев
 *
 * - Перевел Catalogue на MVI
 */