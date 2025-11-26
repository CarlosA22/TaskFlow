package com.taskflow.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.taskflow.app.R
import com.taskflow.app.TaskFlowApplication
import com.taskflow.app.ui.components.TaskItem
import com.taskflow.app.ui.components.TaskStatsBar
import com.taskflow.app.ui.viewmodel.TaskFilter
import com.taskflow.app.ui.viewmodel.TaskViewModel
import com.taskflow.app.ui.viewmodel.TaskViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    userName: String,
    modifier: Modifier = Modifier
) {
    val application = androidx.compose.ui.platform.LocalContext.current.applicationContext as TaskFlowApplication

    val viewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(application.repository)
    )

    val filteredTasks by viewModel.filteredTasks.collectAsStateWithLifecycle(emptyList())
    val currentFilter by viewModel.currentFilter.collectAsStateWithLifecycle()
    val totalTasksCount by viewModel.totalTasksCount.collectAsStateWithLifecycle(0)
    val pendingTasksCount by viewModel.pendingTasksCount.collectAsStateWithLifecycle(0)
    val completedTasksCount by viewModel.completedTasksCount.collectAsStateWithLifecycle(0)
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var newTaskText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Olá, $userName!",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = stringResource(id = R.string.my_tasks),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (newTaskText.isNotBlank()) {
                        viewModel.addTask(newTaskText)
                        newTaskText = ""
                        keyboardController?.hide()
                    }
                },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_task)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            TaskStatsBar(
                totalTasks = totalTasksCount,
                pendingTasks = pendingTasksCount,
                completedTasks = completedTasksCount
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                OutlinedTextField(
                    value = newTaskText,
                    onValueChange = { newTaskText = it },
                    label = { Text(stringResource(id = R.string.task_hint)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (newTaskText.isNotBlank()) {
                                    viewModel.addTask(newTaskText)
                                    newTaskText = ""
                                    keyboardController?.hide()
                                }
                            },
                            enabled = newTaskText.isNotBlank()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(id = R.string.add_task),
                                tint = if (newTaskText.isNotBlank())
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.outline
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (newTaskText.isNotBlank()) {
                                viewModel.addTask(newTaskText)
                                newTaskText = ""
                                keyboardController?.hide()
                            }
                        }
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TaskFilterChips(
                currentFilter = currentFilter,
                onFilterChange = { viewModel.setFilter(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else if (filteredTasks.isEmpty()) {
                EmptyTasksState(currentFilter = currentFilter)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        items = filteredTasks,
                        key = { task -> task.id }
                    ) { task ->
                        TaskItem(
                            task = task,
                            onToggleComplete = { viewModel.toggleTaskCompletion(it) },
                            onDelete = { viewModel.deleteTask(it) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskFilterChips(
    currentFilter: TaskFilter,
    onFilterChange: (TaskFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = currentFilter == TaskFilter.ALL,
            onClick = { onFilterChange(TaskFilter.ALL) },
            label = { Text(stringResource(id = R.string.all_tasks)) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        FilterChip(
            selected = currentFilter == TaskFilter.PENDING,
            onClick = { onFilterChange(TaskFilter.PENDING) },
            label = { Text(stringResource(id = R.string.pending_tasks)) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = com.taskflow.app.ui.theme.TaskPendingOrange,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        FilterChip(
            selected = currentFilter == TaskFilter.COMPLETED,
            onClick = { onFilterChange(TaskFilter.COMPLETED) },
            label = { Text(stringResource(id = R.string.completed_tasks)) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = com.taskflow.app.ui.theme.TaskCompletedGreen,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@Composable
private fun EmptyTasksState(
    currentFilter: TaskFilter,
    modifier: Modifier = Modifier
) {
    val message = when (currentFilter) {
        TaskFilter.ALL -> "Nenhuma tarefa criada ainda.\nAdicione sua primeira tarefa!"
        TaskFilter.PENDING -> "Nenhuma tarefa pendente.\nParabéns! Você está em dia!"
        TaskFilter.COMPLETED -> "Nenhuma tarefa concluída ainda.\nComplete algumas tarefas para vê-las aqui!"
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "📝",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}