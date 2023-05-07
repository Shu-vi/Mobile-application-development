package com.generalov.lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.generalov.lab4.screen.Screen
import com.generalov.lab4.screen.account.Account
import com.generalov.lab4.screen.home.HomePage
import com.generalov.lab4.screen.profile.ProfilePage


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainActivityViewModel: MainActivityViewModel = viewModel()
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = startDestination(mainActivityViewModel)
            ) {
                composable(Screen.Account.route) { Account(navController) }
                composable(Screen.Home.route) { HomePage(navController) }
                composable(Screen.Profile.route) { ProfilePage(navController) }
            }
        }
    }


    private fun startDestination(viewModel: MainActivityViewModel): String {
        val id: Int = viewModel.getUserId()
        return if (id > 0) {
            Screen.Home.route
        } else {
            Screen.Account.route
        }
    }
}