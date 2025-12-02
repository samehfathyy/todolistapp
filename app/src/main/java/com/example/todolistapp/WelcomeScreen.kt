package com.example.todolistapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todolistapp.theme.TaskViewModel
import com.example.todolistapp.ui.theme.*
import com.google.firebase.auth.FirebaseAuth


@Composable
fun WelcomeScreen(navController: NavController,
            viewModel: TaskViewModel,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
//        val auth = FirebaseAuth.getInstance()
//        if (auth.currentUser == null) {
//            // First time ever
//            auth.signInAnonymously()
//                .addOnSuccessListener {
//                    navController.navigate("home")
//                }
//                .addOnFailureListener {
//                    navController.navigate("error")
//                }
//        } else {
//            // User already signed in, no need to sign in again
//        }
    }
    Scaffold(

    ) {
            inner ->
        Column(Modifier.padding(inner)) {
            Text(modifier =  Modifier.fillMaxWidth().padding(top = 60.dp),
                text="Welcome to our app",
                style = TextStyle(fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MainColor))
            Text(modifier =  Modifier.fillMaxWidth().padding(top = 10.dp),
                text="Task Manager",
                style = TextStyle(fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = Color.Black))
            Spacer(Modifier.weight(1f))
            Button(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp),
                onClick = {
                    navController.navigate("home")
                },
                colors = ButtonColors(containerColor = MainColor, contentColor = Color.White, disabledContainerColor = Color.LightGray, disabledContentColor = Color.LightGray),
                ) {
                Text("continue", style = TextStyle(fontSize = 22.sp ))
            }
        }
    }
}