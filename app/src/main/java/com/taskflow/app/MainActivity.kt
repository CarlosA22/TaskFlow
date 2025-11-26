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

/**
 * Atividade principal do aplicativo TaskFlow.
 * Responsável por configurar a navegação, aplicar o tema e inicializar os ViewModels.
 */
class MainActivity : ComponentActivity() {

    /**
     * Instância do MainViewModel.
     * Como o MainViewModel agora tem uma dependência (UserRepository), usamos uma factory
     * para que o sistema saiba como criá-lo. A factory recebe a instância do repositório
     * da nossa classe Application.
     */
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as TaskFlowApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar a interface usando Jetpack Compose
        setContent {
            // Aplicar o tema personalizado do TaskFlow
            TaskFlowTheme {
                // O Surface fornece um contêiner de background com a cor do tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState by mainViewModel.uiState.collectAsState()
                    // Configurar o sistema de navegação do aplicativo
                    TaskFlowNavigation(uiState.userName) { name ->
                        mainViewModel.setUserName(name)
                    }
                }
            }
        }
    }
}

/**
 * Composable responsável pela navegação entre as telas do aplicativo.
 * Gerencia as rotas e a lógica de navegação usando o Navigation Compose.
 *
 * @param userName O nome do usuário atual. Se não estiver vazio, o usuário está logado.
 * @param onLoginSuccess Callback para ser chamado quando o login for bem-sucedido.
 */
@Composable
fun TaskFlowNavigation(
    userName: String,
    onLoginSuccess: (String) -> Unit
) {
    // O NavController é o cérebro da navegação, gerenciando a pilha de telas (back stack).
    val navController = rememberNavController()
    val isLoggedIn = userName.isNotEmpty()

    // O NavHost define o gráfico de navegação.
    // A startDestination é determinada com base no estado de login.
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "task_list" else "login"
    ) {
        // Rota para a tela de login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    // Notifica o ViewModel sobre o login bem-sucedido
                    onLoginSuccess(it)
                    // Navega para a lista de tarefas
                    navController.navigate("task_list") {
                        // Limpa a pilha de navegação até a tela de login (inclusive)
                        // para que o usuário não possa voltar para a tela de login pressionando "voltar".
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Rota para a tela principal de tarefas
        composable("task_list") {
            TaskListScreen(userName = userName)
        }
    }
}
