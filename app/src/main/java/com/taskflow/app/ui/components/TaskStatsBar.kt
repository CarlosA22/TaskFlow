package com.taskflow.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taskflow.app.R
import com.taskflow.app.ui.theme.TaskCompletedGreen
import com.taskflow.app.ui.theme.TaskPendingOrange

/**
 * Componente que exibe estatísticas das tarefas
 * Mostra totais de tarefas organizadas em cards coloridos
 *
 * @param totalTasks Número total de tarefas
 * @param pendingTasks Número de tarefas pendentes
 * @param completedTasks Número de tarefas concluídas
 */
@Composable
fun TaskStatsBar(
    totalTasks: Int,
    pendingTasks: Int,
    completedTasks: Int,
    modifier: Modifier = Modifier
) {
    // Card principal que contém todas as estatísticas
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        // Layout horizontal com espaçamento igual entre os itens
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Estatística: Total de tarefas
            StatItem(
                label = stringResource(id = R.string.total_tasks),
                count = totalTasks,
                color = MaterialTheme.colorScheme.primary
            )

            // Separador vertical
            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp),
                color = MaterialTheme.colorScheme.outline
            )

            // Estatística: Tarefas pendentes
            StatItem(
                label = stringResource(id = R.string.pending),
                count = pendingTasks,
                color = TaskPendingOrange
            )

            // Separador vertical
            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp),
                color = MaterialTheme.colorScheme.outline
            )

            // Estatística: Tarefas concluídas
            StatItem(
                label = stringResource(id = R.string.completed),
                count = completedTasks,
                color = TaskCompletedGreen
            )
        }
    }
}

/**
 * Componente individual de estatística
 * Exibe um número e seu label com cor personalizada
 *
 * @param label Texto descritivo (ex: "Total", "Pendentes")
 * @param count Número a ser exibido
 * @param color Cor do número e do indicador
 */
@Composable
private fun StatItem(
    label: String,
    count: Int,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Número da estatística com destaque visual
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = color.copy(alpha = 0.1f), // Cor de fundo transparente
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Label descritivo
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}