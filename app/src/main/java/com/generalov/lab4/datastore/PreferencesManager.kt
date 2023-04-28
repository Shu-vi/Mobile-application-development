package com.generalov.lab4.datastore

import android.content.Context

class PreferencesManager(context: Context) {
    private val sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

    fun saveUserId(userId: Int) {
        with(sharedPref.edit()) {
            putInt("user_id", userId)
            apply()
        }
    }

    fun getUserId(): Int {
        return sharedPref.getInt("user_id", -1)
    }

    fun removeUserId() {
        with(sharedPref.edit()) {
            remove("user_id")
            apply()
        }
    }
}