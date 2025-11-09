package me.alexandervortex.shelfie.ui.component.refactored

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.base.ext.getStaticSurfaceVariant
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.SHAPE_M
import me.alexandervortex.shelfie.ui.theme.SHAPE_S

private const val BUTTON_GAP = 8
private const val TEXT_GAP = 16
private const val TITLE_GAP = 8
private const val TITLE_SIZE = 21
private const val ROOT_PADDING = 32
private const val BOX_PADDING = 16

@Composable
fun PopupComponent(
    title: String,
    subtitle: String,
    approveText: String,
    declineText: String,
    onApprove: () -> Unit,
    onDecline: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(getStaticSurfaceVariant().copy(alpha = 0.7f))
            .clickable { onDecline.invoke() }
            .padding(ROOT_PADDING.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(SHAPE_M)
                .background(getColors().surface)
                .padding(BOX_PADDING.dp)
        ) {
            Text(text = title, fontSize = TITLE_SIZE.sp)
            Spacer(Modifier.size(TITLE_GAP.dp))
            Text(subtitle)
            Spacer(Modifier.size(TEXT_GAP.dp))
            ButtonComponent(
                shape = SHAPE_S,
                backgroundColor = getColors().error,
                contentColor = getColors().onError,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onApprove.invoke() },
                content = { Text(text = approveText, color = it) }
            )
            Spacer(Modifier.size(BUTTON_GAP.dp))
            ButtonComponent(
                shape = SHAPE_S,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDecline.invoke() },
                content = { Text(text = declineText, color = it) })
        }
    }
}

@CombinedPreviews
@Composable
private fun PopupComponentPreview() {
    CombinedPreviews {
        PopupComponent(
            "Удалить?",
            "Оно удалится безвозвратно",
            "Удалить",
            "Отмена", {}, {}
        )
    }
}