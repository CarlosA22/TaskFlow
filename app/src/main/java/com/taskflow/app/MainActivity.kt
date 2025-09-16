package com.taskflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.taskflow.app.ui.screens.LoginScreen
import com.taskflow.app.ui.screens.TaskListScreen
import com.taskflow.app.ui.theme.TaskFlowTheme

/**
 * Atividade principal do aplicativo TaskFlow
 * Responsável por configurar a navegação e aplicar o tema
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar a interface usando Jetpack Compose
        setContent {
            // Aplicar o tema personalizado do TaskFlow
            TaskFlowTheme {
                // Surface fornece background com cor do tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Configurar sistema de navegação
                    TaskFlowNavigation()
                }
            }
        }
    }
}

/**
 * Composable responsável pela navegação entre telas
 * Gerencia as rotas do aplicativo usando Navigation Compose
 */
@Composable
fun TaskFlowNavigation() {
    // Controlador de navegação - gerencia pilha de telas
    val navController = rememberNavController()

    // Estado para controlar se usuário fez login
    var isLoggedIn by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("") }

    // Configurar as rotas disponíveis
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "task_list" else "login"
    ) {
        // Rota da tela de login
        composable("login") {
            LoginScreen(
                onLoginSuccess = { name ->
                    // Quando login é bem-sucedido, atualizar estado e navegar
                    userName = name
                    isLoggedIn = true
                    navController.navigate("task_list") {
                        // Limpar pilha para não poder voltar ao login
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Rota da tela principal de tarefas
        composable("task_list") {
            TaskListScreen(userName = userName)
        }
    }
}