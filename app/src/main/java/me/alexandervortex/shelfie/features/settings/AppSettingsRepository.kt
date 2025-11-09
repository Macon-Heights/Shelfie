package me.alexandervortex.shelfie.features.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingsRepository
@Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val Context.dataStore by preferencesDataStore(name = "app_settings")

    private object Keys {

        val fontSize = intPreferencesKey("font_size")
    }

    val fontSizeFlow: Flow<Int> = context.dataStore.data
        .map { it[Keys.fontSize] ?: 16 }

    suspend fun setFontSize(size: Int) {
        if (size in 1..199) {
            context.dataStore.edit { it[Keys.fontSize] = size }
        }
    }
}