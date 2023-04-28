package com.generalov.lab4.database.repo


import com.generalov.lab4.database.entity.User
import com.generalov.lab4.database.entity.UserDao

class UserRepository(private val userDao: UserDao) {
    suspend fun getUserByPhone(phone: String): User? {
        return userDao.getUserByPhone(phone)
    }

    suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)
    }

    suspend fun insert(user: User) {
        userDao.insertUser(user)
    }

    suspend fun update(user: User) {
        userDao.updateUser(user)
    }
}