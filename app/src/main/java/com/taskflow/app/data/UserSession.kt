package com.taskflow.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade que representa a sessão do usuário no banco de dados.
 * Usaremos uma única linha nesta tabela para armazenar o usuário logado.
 *
 * @param id A chave primária, que será sempre a mesma (ex: 1) para a única linha.
 * @param userName O nome do usuário logado.
 */
@Entity(tableName = "user_session")
data class UserSession(
    @PrimaryKey val id: Int = 1,
    val userName: String
)
