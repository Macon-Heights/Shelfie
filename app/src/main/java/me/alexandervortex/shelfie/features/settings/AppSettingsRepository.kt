package me.alexandervortex.shelfie.features.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
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
        private val STOPPING_TIME = longPreferencesKey("stopping_time")
        private val LINE_HEIGHT = floatPreferencesKey("line_height")
    }

    val fontSizeFlow = context.dataStore.data.map { prefs ->
        prefs[FONT_SIZE] ?: 16
    }

    val stoppingTimeFlow = context.dataStore.data.map { prefs ->
        prefs[STOPPING_TIME] ?: 0L
    }

    val lineHeightFlow = context.dataStore.data.map { prefs ->
        prefs[LINE_HEIGHT] ?: 1f
    }

    suspend fun setFontSize(value: Int) {
        if (value in 1..200) {
            context.dataStore.edit { it[FONT_SIZE] = value }
        }
    }

    suspend fun setStoppingTime(value: Long) {
        // fixme filter of height here
        context.dataStore.edit { it[STOPPING_TIME] = value }
    }

    suspend fun setLineHeight(value: Float) {
        context.dataStore.edit { it[LINE_HEIGHT] = value }
    }
}