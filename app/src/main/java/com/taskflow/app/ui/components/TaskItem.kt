package com.taskflow.app.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.taskflow.app.data.Task
import com.taskflow.app.ui.theme.TaskCompletedGreen
import com.taskflow.app.ui.theme.TaskDeleteRed
import java.text.SimpleDateFormat
import java.util.*

/**
 * Componente que representa um item de tarefa individual
 * Inclui checkbox, texto, data de criação e botão de delete
 * Com animações para melhorar a experiência do usuário
 *
 * @param task Objeto Task com os dados da tarefa
 * @param onToggleComplete Callback para marcar/desmarcar como concluída
 * @param onDelete Callback para deletar a tarefa
 */
@Composable
fun TaskItem(
    task: Task,
    onToggleComplete: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    // Animação para tarefas concluídas (reduz opacidade)
    val alpha by animateFloatAsState(
        targetValue = if (task.isCompleted) 0.6f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "TaskItemAlpha"
    )

    // Card principal do item de tarefa
    Card(
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted)
                TaskCompletedGreen.copy(alpha = 0.05f)
            else
                MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Lado esquerdo: Checkbox e conteúdo da tarefa
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Checkbox para marcar tarefa como concluída
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggleComplete(task) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = TaskCompletedGreen,
                        uncheckedColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Coluna com título e data da tarefa
                Column(
                    modifier = Modifier.clickable { onToggleComplete(task) }
                ) {
                    // Título da tarefa
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textDecoration = if (task.isCompleted)
                            TextDecoration.LineThrough
                        else
                            TextDecoration.None
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Data de criação formatada
                    Text(
                        text = formatDate(task.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Lado direito: Botão de delete
            IconButton(
                onClick = { onDelete(task) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = TaskDeleteRed
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Deletar tarefa",
                    tint = TaskDeleteRed
                )
            }
        }
    }
}

/**
 * Função utilitária para formatar timestamp em data legível
 * @param timestamp Timestamp em milissegundos
 * @return String formatada da data (ex: "15/09/2025 14:30")
 */
private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(date)
}