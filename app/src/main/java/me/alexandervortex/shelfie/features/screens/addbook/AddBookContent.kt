package me.alexandervortex.shelfie.features.screens.addbook

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.features.screens.addbook.mvi.PreviewScreenUIModel
import me.alexandervortex.shelfie.ui.component.ButtonUI
import me.alexandervortex.shelfie.ui.component.new.CarouselImageUI
import me.alexandervortex.shelfie.ui.component.new.ImageUI
import me.alexandervortex.shelfie.ui.component.new.ImageUIModel
import me.alexandervortex.shelfie.ui.component.new.TitleUI
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.preview.getImages
import me.alexandervortex.shelfie.ui.preview.getTitleInfo

const val COVER_HEIGHT = 256
const val CAROUSEL_HEIGHT = 128
const val HORIZONTAL_PADDING = 32
const val VERTICAL_GAP = 32
const val TITLE_SIZE = 32
const val SUBTITLE_SIZE = 21

@Composable
fun AddBookContent(
    state: PreviewScreenUIModel,
    onImport: () -> Unit
) {
    Column(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState())
    ) {
        state.let { info ->
            ImageUI(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = VERTICAL_GAP.dp)
                    .padding(horizontal = HORIZONTAL_PADDING.dp)
                    .height(COVER_HEIGHT.dp), imageUIModel = info.coverImage
            )
            CarouselImageUI(
                modifier = Modifier
                    .padding(top = VERTICAL_GAP.dp)
                    .padding(horizontal = HORIZONTAL_PADDING.dp)
                    .height(CAROUSEL_HEIGHT.dp), images = info.manyImages
            )
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = VERTICAL_GAP.dp)
            ) {
                TitleUI(text = info.title, size = TITLE_SIZE)
                TitleUI(text = info.author, size = SUBTITLE_SIZE)
                Spacer(Modifier.size(VERTICAL_GAP.dp))
                info.annotation?.let {
                    Text(it)
                    Spacer(Modifier.size(VERTICAL_GAP.dp))
                }
                ButtonUI(modifier = Modifier.fillMaxWidth(), modifierAfter = Modifier.clickable {
                    onImport.invoke()
                }, content = {
                    Text(
                        text = stringResource(R.string.add_book_import_title), color = it
                    )
                })
            }
        }
    }
}

@Preview
@Composable
private fun AddScreenPreview() {
    CombinedPreviews {
        val context = LocalContext.current
        val cover = remember {
            ImageUIModel(context.resources.openRawResource(getImages().random()).readBytes())
        }
        val screens = remember {
            getImages().map { ImageUIModel(context.resources.openRawResource(it).readBytes()) }
        }
        val state = getTitleInfo(cover, screens)
        AddBookContent(state) {}
    }
}