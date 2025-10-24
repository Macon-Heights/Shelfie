package me.alexandervortex.shelfie.features.catalogue

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.features.catalogue.ui.model.UIState
import me.alexandervortex.shelfie.ui.component.ActionButtonComponent
import me.alexandervortex.shelfie.ui.component.BookComponent
import me.alexandervortex.shelfie.ui.component.TitleComponent
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.IC_ADD
import me.alexandervortex.shelfie.ui.theme.getColors

@Composable
@CombinedPreviews
fun CatalogueScreenContentPreview() {
    CombinedPreviews {
        CatalogueScreenContent(
            uiState = UIState.CatalogueBooksState(),
            onAddClick = { },
            onBookClick = { }
        )
    }
}

@Composable
fun CatalogueScreenContent(
    uiState: UIState,
    onBookClick: (BookEntity) -> Unit,
    onAddClick: () -> Unit,
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
            item {
                if (uiState is UIState.CatalogueBooksState && uiState.books.isEmpty()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        color = getColors().onBackground,
                        text = stringResource(R.string.catalogue_empty),
                        fontSize = 32.sp,
                        lineHeight = 56.sp,
                        fontWeight = FontWeight.Thin,
                    )
                }
                if (uiState is UIState.CatalogueLoadingState) {
                    CircularProgressIndicator()
                }
            }
            if (uiState is UIState.CatalogueBooksState) {
                items(uiState.books) { item ->
                    BookComponent(
                        model = item,
                        modifier = Modifier.clickable {
                            onBookClick.invoke(item)
                        })
                }
            }
        }
        ActionButtonComponent(
            modifier = Modifier.padding(32.dp),
            content = {
                Icon(
                    imageVector = IC_ADD,
                    contentDescription = "",
                )
            },
            action = onAddClick
        )
    }
}

@Composable
fun CatalogueScreen(
    viewModel: CatalogueViewModel,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        try {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (_: SecurityException) {
        }
        viewModel.importFromUri(uri)
    }

    LaunchedEffect(Unit) { viewModel.getBookEntities() }

    CatalogueScreenContent(
        uiState = viewModel.uiState.value,
        onBookClick = { book ->
            navController.navigate("mviewer?id=${book.id}")
        },
        onAddClick = {
            picker.launch(arrayOf("text/*", "application/*", "application/octet-stream"))
        }
    )
}