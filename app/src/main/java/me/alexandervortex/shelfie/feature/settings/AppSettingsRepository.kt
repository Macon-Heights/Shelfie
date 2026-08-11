package me.alexandervortex.shelfie.feature.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import me.alexandervortex.shelfie.feature.settings.values.ThemeValue
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("app_settings")

@Singleton
class AppSettingsRepository
@Inject constructor(
    @ApplicationContext private val context: Context,
) {

    companion object {

        private val FONT_SIZE = intPreferencesKey("font_size")
        private val PADDING = intPreferencesKey("padding")
        private val LINE_HEIGHT = floatPreferencesKey("line_height")
        private val THEME = intPreferencesKey("theme")
    }

    val fontSizeFlow = context.dataStore.data.map { prefs ->
        prefs[FONT_SIZE] ?: 16
    }

    val themeFlow = context.dataStore.data.map { prefs ->
        prefs[THEME] ?: 0
    }

    val paddingFlow = context.dataStore.data.map { prefs ->
        prefs[PADDING] ?: 24
    }

    val lineHeightFlow = context.dataStore.data.map { prefs ->
        prefs[LINE_HEIGHT] ?: 1f
    }

    suspend fun setFontSize(value: Int) {
        if (value in 1..240) {
            context.dataStore.edit { it[FONT_SIZE] = value }
        }
    }

    suspend fun setTheme(value: ThemeValue) {
        context.dataStore.edit { it[THEME] = value.value }
    }

    suspend fun setPadding(value: Int) {
        if (value in 0..128) {
            context.dataStore.edit { it[PADDING] = value }
        }
    }

    suspend fun setLineHeight(value: Float) {
        if (value in 0f..4f) {
            context.dataStore.edit { it[LINE_HEIGHT] = value }
        }
    }
}