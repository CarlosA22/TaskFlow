package com.taskflow.app.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.taskflow.app.data.local.dao.TaskDao
import com.taskflow.app.data.local.entity.TaskEntity
import com.taskflow.app.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class TaskRepository(
    private val taskDao: TaskDao,
    private val firestore: FirebaseFirestore
) {

    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()

    suspend fun refreshTasks() {
        try {
            val snapshot = firestore.collection("tasks").get().await()
            val tasks = snapshot.toObjects(Task::class.java)
            val taskEntities = tasks.map {
                TaskEntity(id = it.id, title = it.title, isCompleted = it.isCompleted)
            }
            taskDao.insertAll(taskEntities)
        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun addTask(title: String) {
        try {
            val task = Task(title = title)
            val documentReference = firestore.collection("tasks").add(task).await()
            val id = documentReference.id
            val createdTask = task.copy(id = id)
            taskDao.insertTask(TaskEntity(id = createdTask.id, title = createdTask.title, isCompleted = createdTask.isCompleted))
        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun updateTask(task: Task) {
        try {
            firestore.collection("tasks").document(task.id).set(task).await()
            taskDao.updateTask(TaskEntity(id = task.id, title = task.title, isCompleted = task.isCompleted))
        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun deleteTask(task: Task) {
        try {
            firestore.collection("tasks").document(task.id).delete().await()
            taskDao.deleteTask(TaskEntity(id = task.id, title = task.title, isCompleted = task.isCompleted))
        } catch (e: Exception) {
            // Handle error
        }
    }
}

// Add this to TaskDao.kt
// @Insert(onConflict = OnConflictStrategy.REPLACE)
// suspend fun insertAll(tasks: List<TaskEntity>)
