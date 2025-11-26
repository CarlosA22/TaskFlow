package com.taskflow.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * ViewModel para a tela de login.
 *
 * Gerencia o estado da interface do usuário (UI) para a tela de login,
 * incluindo o nome de usuário inserido e a lógica de validação.
 */
class LoginViewModel : ViewModel() {

    /**
     * O estado da UI para a tela de login.
     */
    var uiState by mutableStateOf(LoginUiState())
        private set

    /**
     * Atualiza o nome de usuário no estado da UI.
     *
     * @param newName O novo nome de usuário inserido.
     */
    fun updateUserName(newName: String) {
        uiState = uiState.copy(userName = newName, isLoginEnabled = newName.trim().isNotEmpty())
    }
}

/**
 * Representa o estado da UI para a tela de login.
 *
 * @property userName O nome de usuário atual inserido.
 * @property isLoginEnabled `true` se o botão de login deve estar ativado, `false` caso contrário.
 */
data class LoginUiState(
    val userName: String = "",
    val isLoginEnabled: Boolean = false
)
