package me.alexandervortex.shelfie.features.viewer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.alexandervortex.shelfie.features.settings.LocalAppSettings
import me.alexandervortex.shelfie.features.settings.SettingsViewModel
import me.alexandervortex.shelfie.features.viewer.mvi.ViewerIntent
import me.alexandervortex.shelfie.features.viewer.mvi.ViewerState
import me.alexandervortex.shelfie.ui.component.ComponentUI
import me.alexandervortex.shelfie.ui.component.PlayerUI
import me.alexandervortex.shelfie.ui.component.PopupBoxUI
import me.alexandervortex.shelfie.ui.component.SettingsUI
import me.alexandervortex.shelfie.ui.model.ElementUIModel
import me.alexandervortex.shelfie.ui.model.getKey
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.preview.getBookUI

@Composable
fun ViewerContent(
    state: ViewerState,
    listState: LazyListState,
    onIntent: (ViewerIntent) -> Unit,
) {
    val book = state.book
    val isSettings = state.isSettingsVisible
    val padding = LocalAppSettings.padding.current
    PopupBoxUI(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd,
        isPopup = isSettings,
        content = {
            LazyColumn(
                userScrollEnabled = !state.serviceState.isPlaying,
                state = listState,
                horizontalAlignment = Alignment.Start,
                contentPadding = PaddingValues(padding.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(tween())
            ) {
                val sections: List<ElementUIModel> = book?.elements.orEmpty()
                itemsIndexed(
                    sections,
                    key = { index, item ->
                        getKey(index, listState.firstVisibleItemIndex)
                    }
                ) { index, section ->

                    AnimatedContent(
                        targetState = section,
                        transitionSpec = {
                            fadeIn(tween()) togetherWith fadeOut(tween())
                        },
                        label = "section_item_$index"
                    ) { animated ->
                        ComponentUI(
                            modifier = Modifier
                                .clickable {
                                    onIntent(ViewerIntent.ToggleMenu)
                                }
                                .animateItem(placementSpec = tween()), // только item placement
                            element = animated,
                            elementIndex = index,
                            currentIndex = state.serviceState.index,
                            partIndex = state.serviceState.part
                        )
                    }
                }
            }

            PlayerUI(
                visible = state.isMenuVisible,
                state = state.serviceState,
                index = remember { derivedStateOf { listState.firstVisibleItemIndex } }.value,
                elements = book?.elements?.size ?: 0,
                settingsAction = { onIntent(ViewerIntent.ToggleSettings) },
                playPauseAction = { onIntent(ViewerIntent.TogglePlayPause(listState.firstVisibleItemIndex)) },
                timerAction = { onIntent(ViewerIntent.ToggleTimer) },
                speedAction = { onIntent(ViewerIntent.ToggleSpeed) },
                prevAction = { onIntent(ViewerIntent.Prev) },
                nextAction = { onIntent(ViewerIntent.Next) }
            )
        },
        popup = {
            val viewModel = hiltViewModel<SettingsViewModel>()
            SettingsUI(viewModel) { onIntent(ViewerIntent.ToggleSettings) }
        }
    )
}

@CombinedPreviews
@Composable
fun MediaViewerPreview() {
    CombinedPreviews {
        val bookUI = getBookUI()
        ViewerContent(
            state = ViewerState(
                isMenuVisible = false,
                book = bookUI
            ),
            listState = LazyListState(),
            onIntent = {}
        )
    }
}