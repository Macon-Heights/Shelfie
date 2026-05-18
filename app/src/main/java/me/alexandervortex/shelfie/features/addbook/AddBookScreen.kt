package me.alexandervortex.shelfie.features.addbook

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.features.addbook.mvi.AddBookEffect
import me.alexandervortex.shelfie.features.addbook.mvi.AddBookIntent
import me.alexandervortex.shelfie.features.addbook.mvi.AddBookState
import me.alexandervortex.shelfie.ui.component.TitleUI
import me.alexandervortex.shelfie.ui.model.TitleInfoUIModel
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews

@Composable
fun AddBookScreen(
    uri: Uri?,
    viewModel: AddBookViewModel,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(AddBookIntent.Add(uri))
        viewModel.effect.collect { effect ->
            when (effect) {
                is AddBookEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()

                is AddBookEffect.NavigateTo ->
                    navController.navigate(effect.route)
            }
        }
    }

    AddBookContent(state)
}

@Composable
private fun AddBookContent(state: AddBookState) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        state.titleInfo?.let { info ->
            info.coverImage?.let { image ->
                val bitmap = BitmapFactory.decodeByteArray(
                    image, 0, image.size
                )

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.padding(32.dp)
                )
            }

            TitleUI("${info.title}\n\n${info.author}")
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
        val state = AddBookState(
            TitleInfoUIModel(
                title = "Причуды природы",
                date = "1981",
                author = "Игорь Акимушкин",
                annotation = "Книга известного популяризатора науки учёного и писателя Игоря Акимушкина посвящена необычным, а порой и парадоксальным явлениям в образе жизни и повадках животного царства природы. Особое внимание уделено редким и исчезающим животным, подлежащим охране. Отдельная глава рассказывает об удивительных феноменах растительного мира Земли. Рассчитана книга на массового читателя.",
                genre = "Природа",
                lang = "ру",
                coverImage = coverBytes,
            )
        )
        AddBookContent(state)
    }
}