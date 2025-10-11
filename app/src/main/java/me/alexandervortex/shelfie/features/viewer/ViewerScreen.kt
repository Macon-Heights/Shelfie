// features/viewer/ViewerScreen.kt
package me.alexandervortex.shelfie.features.viewer

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.features.mediaplayer.TtsViewModel
import me.alexandervortex.shelfie.ui.component.ActionButtonComponent
import me.alexandervortex.shelfie.ui.component.ComponentUI
import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.theme.IC_PAUSE
import me.alexandervortex.shelfie.ui.theme.IC_PLAY

@Composable
fun ViewerScreen(
    id: String,
    viewModel: ViewerBookViewModel,
    ttsVm: TtsViewModel,   // ⬅️ новый VM
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val book = viewModel.bookModel.value

    // грузим книгу
    LaunchedEffect(true) { viewModel.loadCurrentBook(id) }
    LaunchedEffect(book) { ttsVm.loadBook(book) }

    // state из сервиса реактивно
    val serviceState by ttsVm.state.collectAsStateWithLifecycle()

    // UI
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        LazyColumn(
            userScrollEnabled = serviceState.isScrollable,
            state = listState,
            contentPadding = PaddingValues(32.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            val sections: List<ElementUI> = book?.elements.orEmpty()
            itemsIndexed(sections) { index, section ->
                ComponentUI(
                    element = section,
                    elementIndex = index,
                    currentIndex = serviceState.index,
                    partIndex = serviceState.part
                )
            }
        }

        if (serviceState.error.isNotBlank()) {
            LaunchedEffect(serviceState.error) {
                Toast.makeText(context, serviceState.error, Toast.LENGTH_SHORT).show()
            }
        }

        ActionButtonComponent(
            content = {
                Icon(
                    imageVector = if (serviceState.buttonIconRes == R.drawable.ic_pause) IC_PAUSE else IC_PLAY,
                    contentDescription = null
                )
            },
            action = {
                val topIndex = listState.firstVisibleItemIndex
                ttsVm.togglePlayPause(topIndex)
            }
        )
    }
}
