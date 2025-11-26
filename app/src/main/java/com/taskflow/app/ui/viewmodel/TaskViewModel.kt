package com.taskflow.app.ui.viewmodel

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

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _currentFilter = MutableStateFlow(TaskFilter.ALL)
    private val _isLoading = MutableStateFlow(false)

    val currentFilter: StateFlow<TaskFilter> = _currentFilter.asStateFlow()
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

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

    val totalTasksCount = repository.getTotalTasksCount()
    val pendingTasksCount = repository.getPendingTasksCount()
    val completedTasksCount = repository.getCompletedTasksCount()

    fun addTask(title: String) {
        if (title.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newTask = Task(title = title.trim())
                repository.addTask(newTask)
            } catch (e: Exception) {
                // Em uma app real, trataria o erro adequadamente
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            try {
                repository.toggleTaskCompletion(task)
            } catch (e: Exception) {
                // Tratamento de erro
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.deleteTask(task)
            } catch (e: Exception) {
                // Tratamento de erro
            }
        }
    }

    fun setFilter(filter: TaskFilter) {
        _currentFilter.value = filter
    }
}

enum class TaskFilter {
    ALL, PENDING, COMPLETED
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
