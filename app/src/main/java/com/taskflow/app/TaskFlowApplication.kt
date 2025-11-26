package com.taskflow.app

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.taskflow.app.data.local.TaskDatabase
import com.taskflow.app.repository.TaskRepository
import com.taskflow.app.repository.UserRepository

class TaskFlowApplication : Application() {

    val database: TaskDatabase by lazy {
        TaskDatabase.getDatabase(this)
    }

    private val firestore: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    val taskRepository: TaskRepository by lazy {
        TaskRepository(database.taskDao(), firestore)
    }

    val userRepository: UserRepository by lazy {
        UserRepository(database.userSessionDao())
    }
}
