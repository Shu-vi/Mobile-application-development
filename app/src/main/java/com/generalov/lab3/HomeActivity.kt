package com.generalov.lab3

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.generalov.lab3.database.entity.User

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomePage()
        }
    }

    @Composable
    fun HomePage() {
        var user: User? = intent.getSerializableExtra("user") as User?
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    actions = {
                        Button(
                            onClick = {
                                val intent = Intent(HomeActivity@this@HomeActivity, MainActivity::class.java)
                                user = null;
                                startActivity(intent)
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
                                val intent = Intent(HomeActivity@this@HomeActivity, ProfileActivity::class.java)
                                intent.putExtra("user", user)
                                startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Yellow,
                                contentColor = Color.Black
                            )
                        ) {
                            Text(text = user?.username!!)
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
                Text(text = "Welcome!")
            }
        }
    }
}


