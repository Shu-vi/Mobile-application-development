package com.generalov.lab4.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.generalov.lab4.screen.Screen
import com.generalov.lab4.types.JwtState

@Composable
fun HomePage(navController: NavHostController) {
    val viewModel: HomeViewModel = viewModel()
    val user by viewModel.user.collectAsState()
    val jwtState by viewModel.jwtState.collectAsState()

    when(jwtState){
        JwtState.JwtNull -> {
            LaunchedEffect(Unit) {
                logout(viewModel, navController)
            }
        }
        else -> {}
    }
    if (user != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    actions = {
                        Button(
                            onClick = {
                                logout(viewModel, navController)
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Yellow,
                                contentColor = Color.Black
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(text = "Выход")
                        }
                        Button(
                            onClick = {
                                navController.navigate(Screen.Profile.route)
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Yellow,
                                contentColor = Color.Black
                            )
                        ) {
                            user?.let {
                                Text(text = it.username)
                            }
                        }
                    }
                )
            }
        ) { contentPadding ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                Text(text = "Добро пожаловать!")
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

fun logout(viewModel: HomeViewModel, navController: NavHostController){
    viewModel.logout()
    navController.navigate(Screen.Account.route)
}