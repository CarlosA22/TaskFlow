package com.taskflow.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.taskflow.app.data.Task
import com.taskflow.app.data.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * ViewModel que gerencia o estado da tela de tarefas
 * Atua como intermediário entre UI e Repository
 * Sobrevive a mudanças de configuração (rotação de tela, etc.)
 */
class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // Estados privados (mutáveis apenas dentro do ViewModel)
    private val _currentFilter = MutableStateFlow(TaskFilter.ALL)
    private val _isLoading = MutableStateFlow(false)

    // Estados públicos (somente leitura para a UI)
    val currentFilter: StateFlow<TaskFilter> = _currentFilter.asStateFlow()
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Estado combinado das tarefas filtradas
     * Combina lista de tarefas com filtro atual para mostrar apenas as relevantes
     */
    val filteredTasks = combine(
        repository.getAllTasks(),
        _currentFilter
    ) { tasks, filter ->
        when (filter) {
            TaskFilter.ALL -> tasks
            TaskFilter.PENDING -> tasks.filter { !it.isCompleted }
            TaskFilter.COMPLETED -> tasks.filter { it.isCompleted }
        }
    }

    // Estados das estatísticas (observam diretamente o repository)
    val totalTasksCount = repository.getTotalTasksCount()
    val pendingTasksCount = repository.getPendingTasksCount()
    val completedTasksCount = repository.getCompletedTasksCount()

    /**
     * Adiciona nova tarefa ao banco
     * @param title Título da tarefa
     */
    fun addTask(title: String) {
        // Validar entrada
        if (title.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newTask = Task(title = title.trim())
                repository.addTask(newTask)
            } catch (e: Exception) {
                // Em uma app real, trataria o erro adequadamente
                // Por exemplo, mostrando mensagem para o usuário
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Alterna o status de conclusão de uma tarefa
     * @param task Tarefa a ser modificada
     */
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            try {
                repository.toggleTaskCompletion(task)
            } catch (e: Exception) {
                // Tratamento de erro
            }
        }
    }

    /**
     * Remove uma tarefa do banco
     * @param task Tarefa a ser removida
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.deleteTask(task)
            } catch (e: Exception) {
                // Tratamento de erro
            }
        }
    }

    /**
     * Altera o filtro atual das tarefas
     * @param filter Novo filtro a ser aplicado
     */
    fun setFilter(filter: TaskFilter) {
        _currentFilter.value = filter
    }
}

/**
 * Enum que define os tipos de filtro disponíveis
 */
enum class TaskFilter {
    ALL,        // Todas as tarefas
    PENDING,    // Apenas pendentes
    COMPLETED   // Apenas concluídas
}

/**
 * Factory para criar instância do TaskViewModel
 * Necessária porque o ViewModel precisa receber o Repository como parâmetro
 */
class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}