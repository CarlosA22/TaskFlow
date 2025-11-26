package com.taskflow.app

import android.app.Application
import com.taskflow.app.data.TaskDatabase
import com.taskflow.app.data.TaskRepository
import com.taskflow.app.data.UserRepository

/**
 * Classe de aplicação principal do TaskFlow.
 * Responsável por inicializar componentes globais como o banco de dados e os repositórios,
 * garantindo que eles sejam instanciados apenas uma vez durante o ciclo de vida do aplicativo.
 */
class TaskFlowApplication : Application() {

    /**
     * Instância lazy do banco de dados Room.
     * A criação é adiada até o primeiro acesso, o que otimiza a inicialização do app.
     * O `by lazy` garante que a inicialização seja thread-safe.
     */
    val database: TaskDatabase by lazy {
        TaskDatabase.getDatabase(this)
    }

    /**
     * Instância lazy do repositório de tarefas.
     * Depende do DAO de tarefas (`taskDao`) fornecido pelo banco de dados.
     * Centraliza o acesso e a manipulação dos dados das tarefas.
     */
    val taskRepository: TaskRepository by lazy {
        TaskRepository(database.taskDao())
    }

    /**
     * Instância lazy do repositório de sessão do usuário.
     * Depende do DAO de sessão do usuário (`userSessionDao`) fornecido pelo banco de dados.
     * Gerencia a persistência da sessão do usuário.
     */
    val userRepository: UserRepository by lazy {
        UserRepository(database.userSessionDao())
    }
}
