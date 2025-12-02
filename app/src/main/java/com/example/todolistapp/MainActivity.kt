package com.example.todolistapp


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolistapp.data.Task
import com.example.todolistapp.data.TaskDao
import com.example.todolistapp.data.TaskDatabase
import com.example.todolistapp.data.TaskViewModelFactory
import com.example.todolistapp.theme.TaskViewModel
import com.example.todolistapp.ui.theme.TodolistappTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

var dateglobal: Long? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = TaskDatabase.getDatabase(this)
        FirebaseApp.initializeApp(this)
        val factory = TaskViewModelFactory(db.taskDao())
        setContent {
            val navController = rememberNavController()
            val viewModel: TaskViewModel = viewModel(factory = factory)
            var startDestination: String = "home"
            val auth = FirebaseAuth.getInstance()
            if (auth.currentUser == null) {
                // First time ever
                auth.signInAnonymously()
                    .addOnSuccessListener {
//                        Log.d("AUTH", "Anonymous login success: ${auth.uid}")
                        startDestination="welcome"
                    }
                    .addOnFailureListener {
//                        Log.e("AUTH", "Anonymous login failed", it)
                        startDestination="error"
                    }
            } else {
                // User already signed in, no need to sign in again
//                Log.d("AUTH", "User exists: ${auth.uid}")
                startDestination="home"
            }
            NavHost(navController, startDestination = startDestination){
                composable("home"){
                    HomeScreen(navController,viewModel)
                }
                composable("welcome"){
                    WelcomeScreen(navController,viewModel)
                }
                composable("error"){
                    ErrorScreen(navController,viewModel)
                }
                composable("add"){
                    AddTaskScreen(onBack = { navController.popBackStack()},viewModel)
                }
                composable(
                    route = "ViewTask/{taskId}",
                    arguments = listOf(
                        navArgument("taskId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->

                    val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1

                    ViewTaskScreen(
                        onBack = { navController.popBackStack() },
                        taskId,
                        viewModel
                    )
                }

            }
//            TodolistappTheme {
////                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
////                    Greeting(
////                        name = "Android",
////                        modifier = Modifier.padding(innerPadding)
////                    )
////                }
//
//            }
        }
    }
}






@Composable
fun titleandnotetext(text: String,fontSize: Int){
    Text(text = text,
        style = TextStyle(
            fontSize = fontSize.sp,
            color = Color.Black
        )
    )
}
