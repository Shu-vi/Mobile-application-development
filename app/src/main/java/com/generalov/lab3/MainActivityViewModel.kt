package com.generalov.lab3

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.generalov.lab3.datastore.PreferencesManager

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesManager: PreferencesManager

    init {
        preferencesManager = PreferencesManager(application)
    }

    fun getUserId(): Int {
        return preferencesManager.getUserId()
    }
}