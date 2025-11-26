package com.taskflow.app.repository

import com.taskflow.app.data.local.dao.UserSessionDao
import com.taskflow.app.data.local.entity.UserSessionEntity

class UserRepository(private val userSessionDao: UserSessionDao) {

    suspend fun getUserName(): String {
        return userSessionDao.getUserSession()?.userName ?: ""
    }

    suspend fun saveUserName(userName: String) {
        val userSession = UserSessionEntity(userName = userName)
        userSessionDao.saveUserSession(userSession)
    }
}
