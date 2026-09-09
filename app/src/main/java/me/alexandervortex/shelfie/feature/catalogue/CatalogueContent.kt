package me.alexandervortex.shelfie.feature.catalogue

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.feature.catalogue.mvi.CatalogueState
import me.alexandervortex.shelfie.ui.component.BUTTON_BIG
import me.alexandervortex.shelfie.ui.component.ButtonUI
import me.alexandervortex.shelfie.ui.component.CatalogueItemUI
import me.alexandervortex.shelfie.ui.component.ConfirmationUI
import me.alexandervortex.shelfie.ui.component.EmptyStateUI
import me.alexandervortex.shelfie.ui.component.new.TitleUI
import me.alexandervortex.shelfie.ui.model.CatalogueItemUIModel
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.IC_ADD
import me.alexandervortex.shelfie.ui.theme.IC_DELETE
import me.alexandervortex.shelfie.ui.theme.IC_UPDATE

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CatalogueContent(
    state: CatalogueState,
    onBookOpen: (CatalogueItemUIModel.Model) -> Unit,
    onToggleBookCheck: (CatalogueItemUIModel.Model) -> Unit,
    onTogglePopup: (Boolean) -> Unit,
    onToggleRemoveMode: (CatalogueItemUIModel.Model) -> Unit,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit,
    updateClick: () -> Unit,
    onApproveUpdate: () -> Unit,
    onDismissUpdate: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(tween())
        ) {
            item {
                TitleUI(
                    modifier = Modifier.padding(vertical = 64.dp),
                    text = AnnotatedString(stringResource(R.string.catalogue_title))
                )
            }
            when {
                state.books.isEmpty() -> item { EmptyStateUI(R.string.catalogue_empty) }
                else -> {
                    itemsIndexed(
                        state.books,
                        key = { index, _ -> index }
                    ) { index, book ->
                        val bookModifier = if (book is CatalogueItemUIModel.Model) Modifier
                            .combinedClickable(
                                onClick = {
                                    if (state.isRemoveMode) {
                                        onToggleBookCheck(book)
                                    } else {
                                        onBookOpen(book)
                                    }
                                },
                                onLongClick = { onToggleRemoveMode(book) }
                            ) else Modifier

                        AnimatedContent(
                            targetState = book,
                            transitionSpec = {
                                fadeIn(tween()) togetherWith fadeOut(tween())
                            },
                            label = "book_item_$index"
                        ) { animated ->
                            CatalogueItemUI(
                                isRemoveMode = state.isRemoveMode,
                                model = animated,
                                modifier = bookModifier.animateItem(
                                    fadeInSpec = tween(),
                                    fadeOutSpec = tween(),
                                    placementSpec = tween()
                                )
                            )
                        }
                    }
                }
            }
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 64.dp)
                        .padding(bottom = 32.dp)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Bottom
                            )
                        )
                )
            }
        }
        val icon = if (state.isRemoveMode) IC_DELETE else IC_ADD
        val containerColor = if (state.isRemoveMode) getColors().error else null
        val contentColor = if (state.isRemoveMode) getColors().onError else null
            Row(
            modifier = Modifier
                .padding(32.dp)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)),
            ) {
                ButtonUI(
                    contentColor = contentColor,
                    containerColor = containerColor,
                    modifierAfter = Modifier
                        .size(BUTTON_BIG.dp)
                        .clickable {
                            updateClick.invoke()
                        },
                    content = {
                        Icon(
                            imageVector = IC_UPDATE,
                            contentDescription = null,
                            tint = it
                        )
                    }
                )
                Spacer(Modifier.size(32.dp))
                ButtonUI(
            contentColor = contentColor,
            containerColor = containerColor,
            modifierAfter = Modifier
                .size(BUTTON_BIG.dp)
                .clickable {
                    if (state.isRemoveMode) {
                        onTogglePopup.invoke(true)
                    } else {
                        onAddClick.invoke()
                    }
                },
            content = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = it
                )
            }
        )}
        if (state.pendingUpdate != null) {
            ConfirmationUI(
                title = stringResource(R.string.update_dialog_title),
                subtitle = stringResource(R.string.update_dialog_subtitle, state.pendingUpdate.versionName),
                approveText = stringResource(R.string.update_dialog_approve),
                declineText = stringResource(R.string.update_dialog_decline),
                onApprove = onApproveUpdate,
                onDecline = onDismissUpdate
            )
        }
        if (state.isPopup) {
            Dialog(onDismissRequest = { onTogglePopup(false) }) {
                ConfirmationUI(
                    title = stringResource(R.string.catalogue_remove_title),
                    subtitle = stringResource(R.string.catalogue_remove_subtitle),
                    approveText = stringResource(R.string.catalogue_remove_yes),
                    declineText = stringResource(R.string.catalogue_remove_no),
                    onApprove = {
                        onDeleteClick.invoke()
                        onTogglePopup(false)
                    },
                    onDecline = {
                        onTogglePopup(false)
                    }
                )
            }
        }
    }
}

@Composable
@CombinedPreviews
fun PreviewCatalogue() {
    val state = CatalogueState(

    )
    CatalogueContent(
        state = state,
        {}, {}, {},
        {}, {}, {},
        {}, {}, {}
    )
}

@Composable
@CombinedPreviews
fun PreviewCatalogue() {
    val state = CatalogueState(

    )
    CatalogueContent(
        state = state,
        {}, {}, {},
        {}, {}, {},
        {}, {}, {}
    )
}
