package com.taskflow.app.data

import kotlinx.coroutines.flow.Flow

/**
 * Repositório que centraliza o acesso aos dados das tarefas
 * Atua como camada de abstração entre ViewModel e fonte de dados
 * Facilita testes e possíveis mudanças de fonte de dados no futuro
 *
 * @param taskDao DAO para acessar operações do banco de dados
 */
class TaskRepository(private val taskDao: TaskDao) {

    // Operações de consulta - retornam Flow para observação reativa

    /**
     * Obtém todas as tarefas como Flow
     * A UI pode observar mudanças automaticamente
     */
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    /**
     * Obtém apenas tarefas pendentes
     */
    fun getPendingTasks(): Flow<List<Task>> = taskDao.getPendingTasks()

    /**
     * Obtém apenas tarefas concluídas
     */
    fun getCompletedTasks(): Flow<List<Task>> = taskDao.getCompletedTasks()

    // Operações de contagem para estatísticas

    /**
     * Conta total de tarefas
     */
    fun getTotalTasksCount(): Flow<Int> = taskDao.getTotalTasksCount()

    /**
     * Conta tarefas pendentes
     */
    fun getPendingTasksCount(): Flow<Int> = taskDao.getPendingTasksCount()

    /**
     * Conta tarefas concluídas
     */
    fun getCompletedTasksCount(): Flow<Int> = taskDao.getCompletedTasksCount()

    // Operações de modificação - são suspend functions para uso com coroutines

    /**
     * Adiciona nova tarefa
     * @param task Tarefa a ser inserida
     * @return ID da tarefa inserida
     */
    suspend fun addTask(task: Task): Long {
        return taskDao.insertTask(task)
    }

    /**
     * Atualiza tarefa existente
     * Usado principalmente para alterar status de conclusão
     */
    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    /**
     * Remove tarefa do banco
     */
    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    /**
     * Busca tarefa específica por ID
     * @param id ID da tarefa
     * @return Tarefa encontrada ou null
     */
    suspend fun getTaskById(id: Long): Task? {
        return taskDao.getTaskById(id)
    }

    /**
     * Marca tarefa como concluída ou pendente
     * Método de conveniência para toggle do status
     */
    suspend fun toggleTaskCompletion(task: Task) {
        val updatedTask = task.copy(isCompleted = !task.isCompleted)
        updateTask(updatedTask)
    }
}