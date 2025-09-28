package me.alexandervortex.shelfie.base

import android.app.Application
import android.net.Uri
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
 */