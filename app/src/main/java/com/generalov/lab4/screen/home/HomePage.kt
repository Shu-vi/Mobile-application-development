package com.generalov.lab4.screen.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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

    when (jwtState) {
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
            user?.let {
                if (it.isAdmin) {
                    Box(
                        contentAlignment = Alignment.TopCenter,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding)
                    ) {
                        UsersList(viewModel = viewModel)
                    }
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding)
                    ) {
                        Text(text = "Добро пожаловать!")
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

fun logout(viewModel: HomeViewModel, navController: NavHostController) {
    navController.navigate(Screen.Account.route)
    viewModel.logout()
}

@Composable
fun UsersList(viewModel: HomeViewModel) {
    val users by viewModel.users.collectAsState()
    val checkedStates = remember { mutableStateOf(List(users.size) { index -> users[index].isAdmin }) }
    LazyColumn {
        itemsIndexed(users) { index, user ->
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .border(
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                        )
                    )
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    user.id.toString(),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    user.username,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    user.phone,
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = checkedStates.value[index],
                    onCheckedChange = {
                        checkedStates.value = checkedStates.value.mapIndexed { i, value ->
                            if (i == index) it else value
                        }
                        viewModel.roleChange(user)
                    }
                )
            }
        }
    }
}