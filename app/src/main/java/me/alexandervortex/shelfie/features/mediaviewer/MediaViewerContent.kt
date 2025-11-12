package me.alexandervortex.shelfie.features.mediaviewer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.alexandervortex.shelfie.features.settings.LocalAppSettings
import me.alexandervortex.shelfie.features.settings.SettingsComponent
import me.alexandervortex.shelfie.features.settings.SettingsViewModel
import me.alexandervortex.shelfie.ui.component.ComponentUI
import me.alexandervortex.shelfie.ui.component.getBookUI
import me.alexandervortex.shelfie.ui.component.refactored.PopupBox
import me.alexandervortex.shelfie.ui.model.BookUI
import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews

@Composable
fun MediaViewerContent(
    isMenu: Boolean,
    book: BookUI?,
    serviceState: MediaServiceState,
    listState: LazyListState,
    playPauseAction: () -> Unit,
    timerAction: () -> Unit,
    speedAction: () -> Unit,
    prevAction: () -> Unit,
    nextAction: () -> Unit,
    textAction: () -> Unit,
) {
    var isSettings by remember { mutableStateOf(false) }
    val padding = LocalAppSettings.padding.current
    PopupBox(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd,
        isPopup = isSettings,
        content = {
            LazyColumn(
                userScrollEnabled = !serviceState.isPlaying,
                state = listState,
                horizontalAlignment = Alignment.Start,
                contentPadding = PaddingValues(padding.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                val sections: List<ElementUI> = book?.elements.orEmpty()
                itemsIndexed(sections) { index, section ->
                    ComponentUI(
                        modifier = Modifier.clickable { textAction.invoke() },
                        element = section,
                        elementIndex = index,
                        currentIndex = serviceState.index,
                        partIndex = serviceState.part
                    )
                }
            }

            MediaStateComponent(
                visible = isMenu,
                state = serviceState,
                index = remember { derivedStateOf { listState.firstVisibleItemIndex } }.value,
                elements = book?.elements?.size ?: 0,
                settingsAction = { isSettings = !isSettings },
                playPauseAction = { playPauseAction.invoke() },
                timerAction = { timerAction.invoke() },
                speedAction = { speedAction.invoke() },
                prevAction = { prevAction.invoke() },
                nextAction = { nextAction.invoke() }
            )
        },
        popup = {
            val viewModel = hiltViewModel<SettingsViewModel>()
            SettingsComponent(viewModel) { isSettings = false }
        }
    )
}

@CombinedPreviews
@Composable
fun MediaViewerPreview() {
    CombinedPreviews {
        val bookUI = getBookUI()
        MediaViewerContent(
            isMenu = true,
            book = bookUI,
            serviceState = MediaServiceState.playingState(),
            listState = LazyListState(),
            nextAction = {},
            playPauseAction = {},
            prevAction = {},
            textAction = {},
            speedAction = {},
            timerAction = {},
        )
    }
}