package me.alexandervortex.shelfie.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.features.catalogue.CataloguePreviewViewModel

@Composable
fun BookComponent(
    model: BookEntity,
    modifier: Modifier = Modifier,
    navHostController: NavHostController? = null,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
    ) {

        Text(
            model.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        model.author?.let {
            Spacer(Modifier.size(8.dp))
            Text(
                fontWeight = FontWeight.Light,
                text = (it),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}

private fun String.format(): String {
    return replace("""(?<=\b\p{L}{4,}) +(?=\p{L}{1,3}\b)""".toRegex(), "\n")
        .replace("""(?<=\b\p{L}{1,3}) +(?=\p{L}{4,}\b)""".toRegex(), "\n")
}

//@Composable
//@Preview(showBackground = true, widthDp = 120)
//fun BookComponent() {
//    val model = BookEntity(
//        id = 1,
//        fb2DocumentId = "",
//        uri = "",
//        title = "center",
//        author = "",
//        image = null
//    )
//    BookComponent(model)
//}

@Composable
@Preview(showBackground = true)
private fun CatalogueScreen() {
    me.alexandervortex.shelfie.features.catalogue.CatalogueScreen(vm = hiltViewModel<CataloguePreviewViewModel>())
}
