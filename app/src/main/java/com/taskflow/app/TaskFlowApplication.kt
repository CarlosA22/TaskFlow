package com.taskflow.app

import android.app.Application

/**
 * Classe de aplicação principal do TaskFlow
 * Responsável por inicializar componentes globais como o banco de dados
 * É instanciada uma única vez durante o ciclo de vida do app
 */
class TaskFlowApplication : Application() {

    /**
     * Instância lazy do banco de dados
     * Será criada apenas quando acessada pela primeira vez
     * O by lazy garante thread-safety e criação única
     */
    val database by lazy {
        com.taskflow.app.data.TaskDatabase.getDatabase(this)
    }

    /**
     * Instância lazy do repositório
     * Depende do DAO que é obtido do database
     * Centraliza o acesso aos dados das tarefas
     */
    val repository by lazy {
        com.taskflow.app.data.TaskRepository(database.taskDao())
    }
}