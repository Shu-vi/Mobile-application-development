package com.generalov.lab4.datastore

import android.content.Context

class PreferencesManager(context: Context) {
    private val sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    //Управление сессиями
    //Разделение пользователей по ролям - админ и обычный юзер
    //Панель администратора
    fun saveToken(token: String) {
        with(sharedPref.edit()) {
            putString("token", token)
            apply()
        }
    }

    fun getToken(): String? {
        return sharedPref.getString("token", null)
    }

    fun removeToken() {
        with(sharedPref.edit()) {
            remove("token")
            apply()
        }
    }


}