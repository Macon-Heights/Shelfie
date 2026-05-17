package me.alexandervortex.shelfie.features.addbook

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import me.alexandervortex.shelfie.features.addbook.mvi.AddBookEffect
import me.alexandervortex.shelfie.features.addbook.mvi.AddBookIntent
import me.alexandervortex.shelfie.ui.component.TitleUI

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

    Column {
        state.titleInfo?.let { info ->
            info.coverImage?.let { image ->
                val bitmap = BitmapFactory.decodeByteArray(
                    image, 0, image.size
                )

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                )
            }


            TitleUI(info.title)
            TitleUI(info.author)
        }
    }
}