package com.taskflow.app.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * DAO para a entidade UserSession.
 * Define os métodos de acesso ao banco de dados para a sessão do usuário.
 */
@Dao
interface UserSessionDao {

    /**
     * Obtém a sessão do usuário atual.
     * Como só haverá uma linha, busca pela chave primária fixa (1).
     *
     * @return A sessão do usuário, ou null se não houver nenhuma.
     */
    @Query("SELECT * FROM user_session WHERE id = 1")
    suspend fun getUserSession(): UserSession?

    /**
     * Insere ou atualiza a sessão do usuário.
     * O @Upsert é conveniente pois insere se não existir, ou atualiza se já existir.
     *
     * @param userSession O objeto da sessão a ser salvo.
     */
    @Upsert
    suspend fun saveUserSession(userSession: UserSession)
}
