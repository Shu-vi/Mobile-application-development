package com.generalov.lab4.datastore

import android.content.Context

class PreferencesManager(context: Context) {
    private val sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

    fun saveRefreshToken(token: String) {
        with(sharedPref.edit()) {
            putString("refreshToken", token)
            apply()
        }
    }

    fun saveAccessToken(token: String) {
        with(sharedPref.edit()) {
            putString("accessToken", token)
            apply()
        }
    }

    fun getRefreshToken(): String? {
        return sharedPref.getString("refreshToken", null)
    }

    fun getAccessToken(): String? {
        return sharedPref.getString("accessToken", null)
    }

    fun removeAccessToken() {
        with(sharedPref.edit()) {
            remove("accessToken")
            apply()
        }
    }

    fun removeRefreshToken() {
        with(sharedPref.edit()) {
            remove("refreshToken")
            apply()
        }
    }


}