package com.taskflow.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel principal para o aplicativo.
 *
 * Gerencia o estado global do aplicativo, como informações do usuário logado.
 */
class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    /**
     * Define o nome de usuário no estado da UI.
     *
     * @param name O nome de usuário.
     */
    fun setUserName(name: String) {
        _uiState.update { currentState ->
            currentState.copy(userName = name)
        }
    }
}

/**
 * Representa o estado da UI gerenciado pelo [MainViewModel].
 *
 * @property userName O nome do usuário logado.
 */
data class MainUiState(
    val userName: String = ""
)
