package com.example.todolistapp


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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = TaskDatabase.getDatabase(this)

        val factory = TaskViewModelFactory(db.taskDao())
        setContent {
            val navController = rememberNavController()
            val viewModel: TaskViewModel = viewModel(factory = factory)
            NavHost(navController, startDestination = "home"){
                composable("home"){
                    HomeScreen(navController,viewModel)
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

                    ViewTask(
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
@Preview(showBackground = true)
@Composable
fun HomeScreen(navController: NavController,
               viewModel: TaskViewModel,
               ){
    var tasks by remember { mutableStateOf(emptyList<Task>()) }
    @Composable
    fun TaskWidget(title: String,id: Int) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(color = Color.Transparent)
                .padding( bottom = 10.dp)                .clip(RoundedCornerShape(16.dp))

        ){
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(color = Color.DarkGray)
                .padding(start = 20.dp, top = 20.dp, bottom = 20.dp, end = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable()
                {
                    //open new screen with the task
                    navController.navigate("ViewTask/${id}") {
                        launchSingleTop = true
                    }
                }) {
            Row() {
            Text(text = title, style = TextStyle(fontSize = 22.sp, color = Color.White))
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
    }
    Scaffold(topBar = { Text(text = "app") },
        containerColor = Color.LightGray,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add")
                }
                , containerColor =Color.Cyan
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
            verticalArrangement = Arrangement.Center) {
            Text("Your Tasks", style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(10.dp))
            LazyColumn (modifier = Modifier.fillMaxSize()


            ){
                items(tasks) { task ->
                    TaskWidget(title = task.title,task.id)
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AddTaskScreen(onBack:()-> Unit,viewModel: TaskViewModel){
    var taskTitle by remember { mutableStateOf("") }
    var taskNote by remember { mutableStateOf("") }
    val context = LocalContext.current
    Scaffold(
        containerColor = Color.LightGray,
        topBar = {
            TopAppBar(title ={
            Button(
        modifier = Modifier,
        onClick = {onBack()},
                )
            {
        Icon(
            imageVector = Icons.Filled.ArrowBackIosNew,
            contentDescription = "back",
        )
    }//btn
            }
            )

        }
    ) {

            inner ->
        Column(modifier = Modifier
            .padding(inner)
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 20.dp)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Top) {
//            titleandnotetext(taskTitle,24)
//            titleandnotetext(taskNote,20)
            Text("Title", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold ))
            OutlinedTextField(
                value = taskTitle,
                onValueChange = {taskTitle=it},

                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = 24.sp,
                    color = Color.Black
                )
            )
            Text("Note", style = TextStyle(fontSize = 20.sp ))

            OutlinedTextField(
                value = taskNote,
                onValueChange = {taskNote=it},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 20.dp),

                textStyle = TextStyle(
                    fontSize = 22.sp,
                    color = Color.DarkGray
                ),

                )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.White,
                ),
                onClick = {
                //add new task
                if(taskTitle!=""){
                    viewModel.addTask(taskTitle,taskNote);
                    onBack()
                }
                else{
                    Toast.makeText(context,"Please enter task title", Toast.LENGTH_LONG).show()
                }
            }) {
                //button text
                Text(text = "Add Task", style = TextStyle(fontSize = 22.sp ))
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTask(onBack:()-> Unit,taskId: Int,viewModel: TaskViewModel){
    var taskTitle by remember { mutableStateOf("") }
    var taskNote by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getTaskById(taskId) { loadedTasks ->
            taskTitle = loadedTasks?.title?:""
            taskNote = loadedTasks?.note?:""

        }
    }
    Scaffold(
        containerColor = Color.LightGray,
        topBar = {
            TopAppBar(title ={
                Button(
                    modifier = Modifier,
                    onClick = {
                        val updatedTask = Task(taskId,taskTitle,taskNote)
                        viewModel.updateTask(updatedTask)
                        onBack()},
                )
                {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "back",
                    )
                }//btn
            }
            )

        }
    ) {

            inner ->
        //home:
        Column(modifier = Modifier
            .padding(inner)
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Top) {
//            titleandnotetext(taskTitle,24)
//            titleandnotetext(taskNote,20)
            OutlinedTextField(
                value = taskTitle,
                onValueChange = {taskTitle=it},
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = 24.sp,
                    color = Color.Black
                )
            )
            OutlinedTextField(
                value = taskNote,
                onValueChange = {taskNote=it},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 20.dp),

                textStyle = TextStyle(
                    fontSize = 22.sp,
                    color = Color.DarkGray
                ),

            )
            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = {
                //delete the task
                viewModel.deleteTaskById(taskId)
                Toast.makeText(context,"Task deleted successfully", Toast.LENGTH_LONG).show()

                onBack()
            }) {
                //button text
                Text(text = "Delete Task", style = TextStyle(fontSize = 22.sp ))
            }
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
