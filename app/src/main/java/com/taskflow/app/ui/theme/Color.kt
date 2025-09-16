package com.taskflow.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Definição das cores do tema TaskFlow
 * Segue as diretrizes do Material Design 3 com paleta personalizada
 */

// Cores primárias - tom principal do app (azul moderno)
val Primary = Color(0xFF1976D2)      // Azul vibrante
val OnPrimary = Color(0xFFFFFFFF)    // Branco para contraste no primário
val PrimaryContainer = Color(0xFFBBDEFB) // Azul claro para containers
val OnPrimaryContainer = Color(0xFF0D47A1) // Azul escuro para texto em containers

// Cores secundárias - tom de apoio (verde para completadas)
val Secondary = Color(0xFF4CAF50)    // Verde para tarefas concluídas
val OnSecondary = Color(0xFFFFFFFF)  // Branco para contraste
val SecondaryContainer = Color(0xFFC8E6C9) // Verde claro
val OnSecondaryContainer = Color(0xFF1B5E20) // Verde escuro

// Cores terciárias - tom extra (laranja para pendentes)
val Tertiary = Color(0xFFFF9800)     // Laranja para tarefas pendentes
val OnTertiary = Color(0xFFFFFFFF)   // Branco para contraste
val TertiaryContainer = Color(0xFFFFE0B2) // Laranja claro
val OnTertiaryContainer = Color(0xFFE65100) // Laranja escuro

// Cores de erro
val Error = Color(0xFFD32F2F)        // Vermelho para erros
val OnError = Color(0xFFFFFFFF)      // Branco para contraste
val ErrorContainer = Color(0xFFFFCDD2) // Vermelho claro
val OnErrorContainer = Color(0xFFB71C1C) // Vermelho escuro

// Cores de superfície - backgrounds e cards
val Surface = Color(0xFFFAFAFA)      // Cinza muito claro
val OnSurface = Color(0xFF212121)    // Cinza escuro para texto
val SurfaceVariant = Color(0xFFF5F5F5) // Cinza claro para variações
val OnSurfaceVariant = Color(0xFF424242) // Cinza médio

// Cores de outline - bordas e divisores
val Outline = Color(0xFFBDBDBD)      // Cinza médio para bordas
val OutlineVariant = Color(0xFFE0E0E0) // Cinza claro para bordas sutis

// Cores específicas do app
val TaskCompletedGreen = Color(0xFF4CAF50)  // Verde para tarefas completas
val TaskPendingOrange = Color(0xFFFF9800)   // Laranja para tarefas pendentes
val TaskDeleteRed = Color(0xFFE57373)       // Vermelho suave para ação deletar

// Cores para modo escuro
val DarkPrimary = Color(0xFF90CAF9)         // Azul claro para modo escuro
val DarkSurface = Color(0xFF121212)         // Preto para superfície modo escuro
val DarkOnSurface = Color(0xFFE0E0E0)