package com.generalov.lab3.database.repo


import com.generalov.lab3.database.entity.User
import com.generalov.lab3.database.entity.UserDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class UserRepository(private val userDao: UserDao) {

    fun getUser(phone: String): Observable<List<User>> {
        return userDao.getUserByPhone(phone)
    }

    fun insertUser(user: User): Completable {
        return userDao.insertUser(user)
    }

    fun updateUser(user: User): Completable {
        return userDao.updateUser(user)
    }

    fun deleteUser(user: User): Completable {
        return userDao.deleteUser(user)
    }
}