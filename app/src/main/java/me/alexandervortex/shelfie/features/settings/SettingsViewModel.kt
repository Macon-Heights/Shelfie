package me.alexandervortex.shelfie.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: AppSettingsRepository,
) : ViewModel() {

    val state = repo.fontSizeFlow
        .map { SettingsState(fontSize = it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, SettingsState())

    fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ChangeFont -> viewModelScope.launch {
                repo.setFontSize(intent.value)
            }
        }
    }
}
