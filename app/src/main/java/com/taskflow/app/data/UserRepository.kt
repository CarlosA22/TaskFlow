package com.taskflow.app.data

/**
 * Repositório para gerenciar a sessão do usuário.
 * Abstrai a fonte de dados (neste caso, o UserSessionDao) do resto do aplicativo.
 *
 * @property userSessionDao O DAO para acessar os dados da sessão do usuário.
 */
class UserRepository(private val userSessionDao: UserSessionDao) {

    /**
     * Obtém o nome do usuário da sessão atual.
     *
     * @return O nome do usuário, ou uma string vazia se não houver sessão.
     */
    suspend fun getUserName(): String {
        return userSessionDao.getUserSession()?.userName ?: ""
    }

    /**
     * Salva a sessão do usuário.
     *
     * @param userName O nome do usuário a ser salvo.
     */
    suspend fun saveUserName(userName: String) {
        val userSession = UserSession(userName = userName)
        userSessionDao.saveUserSession(userSession)
    }
}
