package me.alexandervortex.shelfie.features.screens.addbook

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.features.screens.addbook.mvi.AddBookUIState
import me.alexandervortex.shelfie.ui.component.ButtonUI
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.preview.getTitleInfo

@Composable
fun AddBookContent(
    state: AddBookUIState,
    onImport: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        state.titleInfo?.let { info ->
            info.coverImage?.let { image ->
                val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.padding(32.dp)
                )
            }

            Text("${info.title}\n${info.author}")
            Spacer(Modifier.size(16.dp))
            Text("${info.annotation}")
            Spacer(Modifier.size(32.dp))
            Row {
                ButtonUI(
                    modifier = Modifier.fillMaxWidth(),
                    modifierAfter = Modifier.clickable {
                        onImport.invoke()
                    },
                    content = {
                        Text(
                            text = stringResource(R.string.add_book_import_title),
                            color = it
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddScreenPreview() {
    CombinedPreviews {
        val context = LocalContext.current

        val coverBytes = remember {
            context.resources.openRawResource(R.drawable.img).readBytes()
        }

        val state = AddBookUIState(
            getTitleInfo(coverBytes)
        )
        AddBookContent(state) {}
    }
}