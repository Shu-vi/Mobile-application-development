package com.generalov.lab4.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.generalov.lab4.types.WeatherData
import com.generalov.lab4.ui.theme.Teal200

@Composable
fun MyListItem(weather: WeatherData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp),
        backgroundColor = Teal200,
        elevation = 0.dp,
        shape = RoundedCornerShape(5.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 5.dp,
                    bottom = 5.dp
                )
            ) {
                Text(text = weather.time)
                Text(
                    text = weather.condition,
                    color = Color.White
                )
            }
            Text(
                text = weather.currentTemp,
                color = Color.White,
                style = TextStyle(fontSize = 25.sp)
            )
            SubcomposeAsyncImage(
                loading = { CircularProgressIndicator() },
                model = "https:${weather.icon}",
                contentDescription = "im5",
                modifier = Modifier
                    .padding(
                        end = 8.dp
                    )
                    .size(35.dp)
            )
        }
    }
}