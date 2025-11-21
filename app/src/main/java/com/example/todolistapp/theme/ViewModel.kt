package com.example.todolistapp.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.data.Task
import com.example.todolistapp.data.TaskDao
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class TaskViewModel(private val dao: TaskDao) : ViewModel() {

    fun addTask(title: String, note: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertTask(Task(title = title, note = note))
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateTask(task)
        }
    }

    fun getAllTasks(onResult: (List<Task>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val tasks = dao.getAllTasks()
            onResult(tasks)
        }
    }


    fun getTaskById(id: Int, onResult: (Task?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = dao.getTask(id)
            onResult(task)
        }
    }
    fun deleteTaskById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = dao.deleteTaskById(id)
        }
    }

}
