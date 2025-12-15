package com.example.todolistapp

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todolistapp.data.Task
import com.example.todolistapp.theme.TaskViewModel
import com.example.todolistapp.ui.theme.MainColor
import com.google.type.DateTime
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController,
               viewModel: TaskViewModel,
){
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    var tasks by remember { mutableStateOf(emptyList<Task>()) }
    var lastsyncdate by remember { mutableStateOf("") }
    @Composable
    fun TaskWidget(title: String,id: Int) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(color = Color.Transparent)
                .padding( bottom = 10.dp).clip(RoundedCornerShape(16.dp)).clickable()
                {
                    //open new screen with the task
                    navController.navigate("ViewTask/${id}") {
                        launchSingleTop = true
                    }
                }

        ){
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(color = Color.LightGray)
                    .padding(start = 20.dp, top = 20.dp, bottom = 20.dp, end = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                  ) {
                Row() {
                    Text(text = title, style = TextStyle(fontSize = 22.sp, color = Color.Black))
                    Spacer(Modifier.weight(1f))
                    Icon(imageVector = Icons.Default.ArrowForwardIos, modifier = Modifier, contentDescription = "")
                }
            }
        }
    }
    //LaunchedEffect is like initstate
    LaunchedEffect(Unit) {
        viewModel.getAllTasks { loadedTasks ->
            tasks = loadedTasks
        }
        lastsyncdate = sharedPref.getString("lastsyncdate","No Sync yet")?:"No Sync yet"
    }
    Scaffold(topBar = { Text(text = "app") },
        containerColor = Color.White,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
//                    tasks += Task(0,"new","")
                    navController.navigate("add")
//                    val intent = Intent()
//                    intent.setClassName(
//                        "com.example.budgeta",
//                        "com.example.budgeta.MainActivity"
//                    )
//                    startActivity(context,intent,null)
                }
                , containerColor = MainColor
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",

                    )
            }
        }
    ) {

            inner ->
        Column(modifier = Modifier
            .padding(inner)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .fillMaxHeight(),
            verticalArrangement = Arrangement.Top) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement =  Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Your Tasks", style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold))
                //sync btn
                IconButton(onClick = {
                    //sync tasks
                    viewModel.viewModelScope.launch {
                        viewModel.SyncWithFirebase()
                        viewModel.getAllTasks { loadedTasks ->
                            tasks = loadedTasks
                        }
                    }
                    val formatter = DateTimeFormatter.ofPattern("d/M/yyyy h:mm a")
                    val current = LocalDateTime.now()
                    val formattedDate = current.format(formatter)
                    lastsyncdate=formattedDate
                    sharedPref.edit()
                        .putString("lastsyncdate", formattedDate)
                        .apply()
                }) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = "sync",
                        )
                }
            }
            Spacer(Modifier.height(10.dp))
            LazyColumn (
//                modifier = Modifier.fillMaxSize()
            ){
                items(tasks) {
                        task ->
                    TaskWidget(title = task.title,task.id)
                }
            }
            Text(text= if(tasks.isEmpty()) "No tasks yet" else "",
                style = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center),
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp))
            Text(text =  "Last Sync date: $lastsyncdate", Modifier.fillMaxWidth(), style = TextStyle(textAlign = TextAlign.Center, fontSize = 18.sp))
        }
    }
}