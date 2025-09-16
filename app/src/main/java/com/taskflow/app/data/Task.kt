package com.taskflow.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade Room que representa uma tarefa no banco de dados
 * Cada instância corresponde a uma linha na tabela 'tasks'
 *
 * @param id Chave primária única, gerada automaticamente
 * @param title Título da tarefa (obrigatório)
 * @param isCompleted Status de conclusão da tarefa
 * @param createdAt Timestamp de criação em milissegundos
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // Valor padrão para novas tarefas

    val title: String,

    val isCompleted: Boolean = false, // Por padrão, tarefas são criadas como pendentes

    val createdAt: Long = System.currentTimeMillis() // Timestamp de criação automático
)