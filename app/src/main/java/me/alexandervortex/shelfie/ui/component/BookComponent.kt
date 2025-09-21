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
import me.alexandervortex.shelfie.features.catalogue.BookPreviewFactory
import me.alexandervortex.shelfie.features.catalogue.CataloguePreviewViewModel

@Composable
fun BookComponent(
    model: BookEntity,
    modifier: Modifier = Modifier,
    navHostController: NavHostController? = null,
) {
    Column(
        modifier = modifier
//            .sizeIn(minHeight = 120.dp)
            .clip(RoundedCornerShape(16.dp))
//            .border(1.dp, Color(0xFF_777777), RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
    ) {
// если 4-5 символов и 1 слово - добавим неразрывных пробелов по-кайфу
// если есть слово из 3 и меньше символов - добавь перенос до и после по-кайфу

        Text(
            model.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Justify
        )
        model.author?.let {
            Spacer(Modifier.size(8.dp))
            Text(
                text = (it),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 120, heightDp = 180)
fun BookComponent() {
    val model = BookPreviewFactory.getBooks().random()
    BookComponent(model)
}

@Composable
@Preview(showBackground = true)
private fun CatalogueScreen() {
    me.alexandervortex.shelfie.features.catalogue.CatalogueScreen(vm = hiltViewModel<CataloguePreviewViewModel>())
}