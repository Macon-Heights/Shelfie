package me.alexandervortex.shelfie.features.welcome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun WelcomeScreen(
    navController: NavHostController,
    viewModel: WelcomeViewModel,
) {
    Column {
        Text("This is welcome screen.")
        Spacer(Modifier.size(64.dp))
        Button({ navController.navigate("filePicker") }) { Text("Go to filePicker screen") }
        Button({ navController.navigate("viewer") }) { Text("Go to a viewer screen") }
    }
}