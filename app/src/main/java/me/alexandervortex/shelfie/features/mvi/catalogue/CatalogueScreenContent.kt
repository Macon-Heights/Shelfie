package me.alexandervortex.shelfie.features.mvi.catalogue

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.features.mediaviewer.RoundButton
import me.alexandervortex.shelfie.features.mvi.catalogue.mvi.CatalogueState
import me.alexandervortex.shelfie.ui.component.BookComponent
import me.alexandervortex.shelfie.ui.component.BookComponentModel
import me.alexandervortex.shelfie.ui.component.TitleComponent
import me.alexandervortex.shelfie.ui.theme.IC_ADD
import me.alexandervortex.shelfie.ui.theme.IC_DELETE
import me.alexandervortex.shelfie.ui.theme.getColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CatalogueScreenContent(
    state: CatalogueState,
    onBookClick: (BookComponentModel) -> Unit,
    onBookLongClick: (BookComponentModel) -> Unit,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier.aspectRatio(1f)
                ) {
                    TitleComponent(text = stringResource(R.string.catalogue_title))
                }
            }
            when {
                state.isLoading -> item { CircularProgressIndicator() }
                state.books.isEmpty() -> item { EmptyList(R.string.catalogue_empty) }

                else -> {
                    items(state.books) { book ->
                        BookComponent(
                            isRemoveMode = state.isRemoveMode,
                            model = book,
                            modifier = Modifier.combinedClickable(
                                onClick = { onBookClick(book) },
                                onLongClick = { onBookLongClick(book) }
                            )
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(96.dp)) }
        }

        RoundButton(
            modifier = Modifier
                .padding(32.dp)
                .windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                WindowInsetsSides.Bottom
                )
                ),
            icon = if (state.isRemoveMode) IC_DELETE else IC_ADD,
            action = if (state.isRemoveMode) onDeleteClick else onAddClick,
            isPrimary = true
        )
    }
}