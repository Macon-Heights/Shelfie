package me.alexandervortex.shelfie.feature.viewer

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
import me.alexandervortex.shelfie.feature.settings.LocalAppSettings
import me.alexandervortex.shelfie.feature.settings.SettingsViewModel
import me.alexandervortex.shelfie.ui.preview.ViewerPreviewData.getBookDocument
import me.alexandervortex.shelfie.feature.viewer.mvi.ViewerIntent
import me.alexandervortex.shelfie.feature.viewer.mvi.ViewerState
import me.alexandervortex.shelfie.model.ParsedBookModel
import me.alexandervortex.shelfie.model.ProgressBookModel
import me.alexandervortex.shelfie.model.ProgressModel
import me.alexandervortex.shelfie.ui.component.ComponentUI
import me.alexandervortex.shelfie.ui.component.PlayerUI
import me.alexandervortex.shelfie.ui.component.PopupBoxUI
import me.alexandervortex.shelfie.ui.component.SectionsUI
import me.alexandervortex.shelfie.ui.component.SettingsUI
import me.alexandervortex.shelfie.ui.model.UI
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.preview.getTitleInfo

@Composable
fun ViewerContent(
    state: ViewerState,
    listState: LazyListState,
    onIntent: (ViewerIntent) -> Unit,
) {
    val book = state.book
    val isPopup = state.isSettingsVisible || state.isSectionsVisible
    val padding = LocalAppSettings.padding.current
    PopupBoxUI(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd,
        isPopup = isPopup,
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
                val sections: List<UI> = book?.elements.orEmpty()
                itemsIndexed(
                    sections,
                    key = { index, item ->
                        index
                    }
                ) { index, section ->

                    AnimatedContent(
                        targetState = section,
                        transitionSpec = {
                            fadeIn(tween()) togetherWith fadeOut(tween())
                        },
                        label = "section_item_$index"
                    ) { animated ->
                        val isCurrentElement = state.serviceState.index == index
                        ComponentUI(
                            modifier = Modifier
                                .clickable {
                                    onIntent(ViewerIntent.ToggleMenu)
                                }
                                .animateItem(placementSpec = tween()),
                            element = animated,
                            isCurrentElement = isCurrentElement,
                            partIndex = state.serviceState.offset
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
                sectionsAction = { onIntent(ViewerIntent.Sections) },
                nextAction = { onIntent(ViewerIntent.Next) }
            )
        },
        popup = {
            if (state.isSettingsVisible) {
                val viewModel = hiltViewModel<SettingsViewModel>()
                SettingsUI(viewModel) { onIntent(ViewerIntent.ToggleSettings) }
            }

            if (state.isSectionsVisible) {
                SectionsUI(
                    state.book?.elements.orEmpty()
                ) { onIntent(ViewerIntent.ToggleSections) }
            }
        }
    )
}

@CombinedPreviews
@Composable
fun MediaViewerPreview() {
    CombinedPreviews {
        val factory = ViewerUIFactory()

        val parsedBookModel = ParsedBookModel(
            titleInfo = getTitleInfo(),
            document = getBookDocument()
        )
        val progressBookModel = ProgressBookModel(
            id = "",
            localPath = "",
            progress = ProgressModel(),
            book = parsedBookModel
        )
        val bookUI = factory.getBookUIModel(progressBookModel)

        ViewerContent(
            state = ViewerState(
                isSectionsVisible = true,
                book = bookUI,
            ),
            listState = LazyListState(),
            onIntent = {}
        )
    }
}