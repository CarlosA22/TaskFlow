package com.taskflow.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Esquema de cores para modo claro
 * Define todas as cores usadas no tema claro do app
 */
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,

    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,

    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,

    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,

    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,

    outline = Outline,
    outlineVariant = OutlineVariant
)

/**
 * Esquema de cores para modo escuro
 * Define todas as cores usadas no tema escuro do app
 */
private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = OnPrimary,
    primaryContainer = OnPrimaryContainer,
    onPrimaryContainer = PrimaryContainer,

    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = OnSecondaryContainer,
    onSecondaryContainer = SecondaryContainer,

    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = OnTertiaryContainer,
    onTertiaryContainer = TertiaryContainer,

    error = Error,
    onError = OnError,
    errorContainer = OnErrorContainer,
    onErrorContainer = ErrorContainer,

    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = OnSurface,
    onSurfaceVariant = Surface,

    outline = Outline,
    outlineVariant = OutlineVariant
)

/**
 * Composable principal do tema TaskFlow
 * Aplica cores, tipografia e configura a barra de status
 *
 * @param darkTheme Se deve usar tema escuro (detecta automaticamente do sistema)
 * @param dynamicColor Se deve usar cores dinâmicas do Android 12+ (Material You)
 * @param content Conteúdo da aplicação que receberá o tema
 */
@Composable
fun TaskFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Disponível no Android 12+
    content: @Composable () -> Unit
) {
    // Escolher esquema de cores baseado nas configurações
    val colorScheme = when {
        // Android 12+ com cores dinâmicas habilitadas
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        // Tema escuro padrão
        darkTheme -> DarkColorScheme

        // Tema claro padrão
        else -> LightColorScheme
    }

    // Obter referências para configurar barra de status
    val view = LocalView.current

    // Configurar barra de status quando o tema for aplicado
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // Definir cor da barra de status
            window.statusBarColor = colorScheme.primary.toArgb()

            // Configurar ícones da barra de status (claro/escuro)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    // Aplicar o tema Material 3 com configurações personalizadas
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}