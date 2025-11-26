package com.taskflow.app.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.taskflow.app.data.local.entity.UserSessionEntity

@Dao
interface UserSessionDao {

    @Query("SELECT * FROM user_session WHERE id = 1")
    suspend fun getUserSession(): UserSessionEntity?

    @Upsert
    suspend fun saveUserSession(userSession: UserSessionEntity)
}
