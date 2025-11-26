package com.taskflow.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.taskflow.app.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel principal para o aplicativo.
 *
 * Gerencia o estado global do aplicativo, como informações do usuário logado.
 * Ele interage com o `UserRepository` para carregar e salvar a sessão do usuário.
 *
 * @property userRepository O repositório para gerenciar os dados da sessão do usuário.
 */
class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        // Ao inicializar o ViewModel, tenta carregar a sessão do usuário do banco de dados.
        viewModelScope.launch {
            val userName = userRepository.getUserName()
            if (userName.isNotEmpty()) {
                _uiState.update { it.copy(userName = userName) }
            }
        }
    }

    /**
     * Define o nome de usuário no estado da UI e o salva no banco de dados.
     *
     * @param name O nome de usuário a ser salvo.
     */
    fun setUserName(name: String) {
        // Primeiro, atualiza o estado da UI otimisticamente.
        _uiState.update { it.copy(userName = name) }

        // Em seguida, inicia uma coroutine para salvar o nome no banco de dados.
        viewModelScope.launch {
            userRepository.saveUserName(name)
        }
    }
}

/**
 * Representa o estado da UI gerenciado pelo [MainViewModel].
 *
 * @property userName O nome do usuário logado. Pode ser uma string vazia se
 *                      nenhum usuário estiver logado ou se a sessão ainda não foi carregada.
 */
data class MainUiState(
    val userName: String = ""
)

/**
 * Factory para criar instâncias do `MainViewModel` com dependências.
 * Como o `MainViewModel` agora requer um `UserRepository` em seu construtor,
 * precisamos de uma factory para que o sistema do Android saiba como criá-lo.
 *
 * @property userRepository O repositório a ser injetado no `MainViewModel`.
 */
class MainViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
