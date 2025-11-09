package me.alexandervortex.shelfie.ui.component.refactored

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.SHAPE_M
import me.alexandervortex.shelfie.ui.theme.getColors

@Composable
fun PopupComponent(
    title: String,
    subtitle: String,
    approveText: String,
    declineText: String,
    onApprove: () -> Unit,
    onDecline: () -> Unit,
) {
    val overlayColor = if (isSystemInDarkTheme()) {
        getColors().surfaceVariant.copy(alpha = 0.7f)
    } else {
        getColors().onSurfaceVariant.copy(alpha = 0.7f)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(overlayColor)
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(SHAPE_M)
                .background(getColors().surface)
                .padding(16.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            Spacer(Modifier.size(8.dp))
            Text(subtitle)
            Spacer(Modifier.size(16.dp))
            Button(
                onClick = onApprove,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(approveText)
            }
            Spacer(Modifier.size(8.dp))
            Button(
                onClick = onDecline,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(declineText)
            }
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
            "da",
            "net", {}, {}
        )
    }
}