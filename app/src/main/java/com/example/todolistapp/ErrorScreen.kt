package com.example.todolistapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolistapp.theme.TaskViewModel


@Composable
fun ErrorScreen(navController: NavController,
                  viewModel: TaskViewModel,
) {
    val context = LocalContext.current
    Scaffold() {
            inner ->
        Column(Modifier.padding(inner)) {
            Text("error")
            Button(
                modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp),
                onClick = {
                    navController.navigate("home")
                },
                colors = ButtonColors(containerColor = Color.Green, contentColor = Color.White, disabledContainerColor = Color.LightGray, disabledContentColor = Color.LightGray),
            ) {
                Text("continue")
            }
        }
    }
}