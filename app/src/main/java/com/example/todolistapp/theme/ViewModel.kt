package com.example.todolistapp.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.data.Task
import com.example.todolistapp.data.TaskDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await

class TaskViewModel(private val dao: TaskDao) : ViewModel() {

    fun addTask(title: String, note: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val daoTask = Task(title = title, note = note)

            // Insert into Room and get generated ID
            val generatedId = dao.insertTask(daoTask)  // returns Long

            // Create a new task with the generated ID
            val taskWithId = daoTask.copy(id = generatedId.toInt())

            // Upload to Firebase with the same ID
            val auth = FirebaseAuth.getInstance()
            val uid = auth.currentUser!!.uid

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("tasks")
                .document(taskWithId.id.toString()) // Use ID as document ID
                .set(taskWithId.toMap())
        }
    }

    suspend fun SyncWithFirebase(){
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid ?: return

        val firestore = FirebaseFirestore.getInstance()
        val tasksCollection = firestore.collection("users").document(uid).collection("tasks")
            try {
                // Fetch all tasks from Firestore
                val snapshot = tasksCollection.get().await()
                val firestoreTasks = snapshot.documents.mapNotNull { doc ->
                    try {
                        val id = doc.getLong("id")?.toInt() ?: return@mapNotNull null
                        val title = doc.getString("title") ?: ""
                        val note = doc.getString("note") ?: ""
                        Task(id, title, note)
                    } catch (e: Exception) {
                        null
                    }
                }

                // Insert new tasks into local DB
                     dao.insertTasks(firestoreTasks)

            } catch (e: Exception) {
                e.printStackTrace()
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
