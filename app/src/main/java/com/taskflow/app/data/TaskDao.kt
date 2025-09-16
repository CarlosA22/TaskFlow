package com.taskflow.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para operações com a entidade Task
 * Define todos os métodos de acesso ao banco de dados
 * Room gera automaticamente a implementação desta interface
 */
@Dao
interface TaskDao {

    /**
     * Busca todas as tarefas ordenadas por data de criação (mais recentes primeiro)
     * Retorna Flow para observar mudanças automaticamente na UI
     */
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>

    /**
     * Busca apenas tarefas pendentes (não concluídas)
     * Útil para filtros na interface
     */
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY createdAt DESC")
    fun getPendingTasks(): Flow<List<Task>>

    /**
     * Busca apenas tarefas concluídas
     * Útil para filtros na interface
     */
    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY createdAt DESC")
    fun getCompletedTasks(): Flow<List<Task>>

    /**
     * Conta o total de tarefas
     * Usado para estatísticas na interface
     */
    @Query("SELECT COUNT(*) FROM tasks")
    fun getTotalTasksCount(): Flow<Int>

    /**
     * Conta tarefas pendentes
     * Usado para estatísticas na interface
     */
    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 0")
    fun getPendingTasksCount(): Flow<Int>

    /**
     * Conta tarefas concluídas
     * Usado para estatísticas na interface
     */
    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1")
    fun getCompletedTasksCount(): Flow<Int>

    /**
     * Insere uma nova tarefa no banco
     * Retorna o ID da tarefa inserida
     */
    @Insert
    suspend fun insertTask(task: Task): Long

    /**
     * Atualiza uma tarefa existente
     * Usado principalmente para marcar/desmarcar como concluída
     */
    @Update
    suspend fun updateTask(task: Task)

    /**
     * Deleta uma tarefa do banco
     */
    @Delete
    suspend fun deleteTask(task: Task)

    /**
     * Busca uma tarefa específica pelo ID
     * Retorna null se não encontrar
     */
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): Task?
}