package com.taskflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.taskflow.app.ui.screens.LoginScreen
import com.taskflow.app.ui.screens.TaskListScreen
import com.taskflow.app.ui.theme.TaskFlowTheme
import com.taskflow.app.ui.viewmodel.MainViewModel
import com.taskflow.app.ui.viewmodel.MainViewModelFactory
import com.taskflow.app.ui.viewmodel.TaskViewModel
import com.taskflow.app.ui.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as TaskFlowApplication).userRepository)
    }

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as TaskFlowApplication).taskRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TaskFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState by mainViewModel.uiState.collectAsState()
                    TaskFlowNavigation(
                        userName = uiState.userName,
                        taskViewModel = taskViewModel,
                        onLoginSuccess = { name ->
                            mainViewModel.setUserName(name)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskFlowNavigation(
    userName: String,
    taskViewModel: TaskViewModel,
    onLoginSuccess: (String) -> Unit
) {
    val navController = rememberNavController()
    val isLoggedIn = userName.isNotEmpty()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "task_list" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    onLoginSuccess(it)
                    navController.navigate("task_list") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("task_list") {
            TaskListScreen(
                userName = userName,
                taskViewModel = taskViewModel
            )
        }
    }
}
