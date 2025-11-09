package me.alexandervortex.shelfie.features.mvi.catalogue

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.features.mvi.catalogue.mvi.CatalogueState
import me.alexandervortex.shelfie.ui.component.BookComponent
import me.alexandervortex.shelfie.ui.component.EmptyList
import me.alexandervortex.shelfie.ui.component.refactored.BUTTON_BIG
import me.alexandervortex.shelfie.ui.component.refactored.ButtonComponent
import me.alexandervortex.shelfie.ui.component.refactored.ConfirmationComponent
import me.alexandervortex.shelfie.ui.component.refactored.PopupBox
import me.alexandervortex.shelfie.ui.component.refactored.TitleComponent
import me.alexandervortex.shelfie.ui.model.BookComponentModel
import me.alexandervortex.shelfie.ui.theme.IC_ADD
import me.alexandervortex.shelfie.ui.theme.IC_DELETE

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CatalogueScreenContent(
    state: CatalogueState,
    onBookOpen: (BookComponentModel) -> Unit,
    onToggleBookCheck: (BookComponentModel) -> Unit,
    onTogglePopup: (Boolean) -> Unit,
    onToggleRemoveMode: (BookComponentModel) -> Unit,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    PopupBox(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd,
        isPopup = state.isPopup,
        content = {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item { TitleComponent(text = stringResource(R.string.catalogue_title)) }
                when {
                    state.isLoading -> item { CircularProgressIndicator() }
                    state.books.isEmpty() -> item { EmptyList(R.string.catalogue_empty) }

                    else -> {
                        items(state.books) { book ->
                            BookComponent(
                                isRemoveMode = state.isRemoveMode,
                                model = book,
                                modifier = Modifier.combinedClickable(
                                    onClick = {
                                        if (state.isRemoveMode) {
                                            onToggleBookCheck(book)
                                        } else {
                                            onBookOpen(book)
                                        }
                                    },
                                    onLongClick = { onToggleRemoveMode(book) })
                            )
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

            ButtonComponent(
                modifier = Modifier
                    .padding(32.dp)
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)),
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
            )
        },
        popup = {
            ConfirmationComponent(
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
    )
}
