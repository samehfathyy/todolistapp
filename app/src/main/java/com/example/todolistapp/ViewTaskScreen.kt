package com.example.todolistapp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistapp.data.Task
import com.example.todolistapp.theme.TaskViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTaskScreen(onBack:()-> Unit,taskId: Int,viewModel: TaskViewModel){
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
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.LightGray,
                    navigationIconContentColor = Color.LightGray,
                    titleContentColor = Color.LightGray,
                    actionIconContentColor = Color.Green
                ),
                title ={
                    Button(
                        colors = ButtonColors(containerColor = Color.Transparent, contentColor = Color.Black, disabledContainerColor = Color.LightGray, disabledContentColor = Color.LightGray),
                        modifier = Modifier.padding(0.dp),
                        contentPadding = PaddingValues(0.dp),

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
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 10.dp)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Top) {
//            titleandnotetext(taskTitle,24)
//            titleandnotetext(taskNote,20)
            Text("Title", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold ))
            Spacer(Modifier.height(10.dp))
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
            Spacer(Modifier.height(20.dp))
            Text("Note", style = TextStyle(fontSize = 22.sp, ))
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = taskNote,
                onValueChange = {taskNote=it},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),

                textStyle = TextStyle(
                    fontSize = 22.sp,
                    color = Color.DarkGray
                ),

                )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp),
                colors = ButtonColors(containerColor = Color.LightGray, contentColor = Color.Red, disabledContainerColor = Color.LightGray, disabledContentColor = Color.LightGray),

                onClick = {
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
