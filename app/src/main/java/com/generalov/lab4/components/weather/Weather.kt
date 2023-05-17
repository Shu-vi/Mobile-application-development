package com.generalov.lab4.components.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.generalov.lab4.R
import com.generalov.lab4.components.DialogSearch
import com.generalov.lab4.components.MyListItem
import com.generalov.lab4.ui.theme.Purple200


@Composable
@SuppressLint("MissingPermission")
fun Weather() {
    val viewModel: WeatherViewModel = viewModel()
    val requestState by viewModel.requestState.collectAsState()

    val dialogState = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var location by remember { mutableStateOf<Location?>(null) }

    val locationListener = object : LocationListener {
        override fun onLocationChanged(newLocation: Location) {
            location = newLocation
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000L,
                    0f,
                    locationListener
                )
            }
        }



    LaunchedEffect(location) {
        if (location == null) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0f,
                    locationListener
                )
            } else {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    val currentDay by viewModel.weatherData.observeAsState()

    if (dialogState.value) {
        DialogSearch(dialogState, onSubmit = {
            viewModel.updateWeather(it)
        })
    }
    if (location == null) {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location == null) {
                LaunchedEffect(location) {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        } else {
            LaunchedEffect(location) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
    if (requestState == RequestState.Error){
        Text(text = "Ошибка при запросе к серверу")
    } else if (currentDay != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(), backgroundColor = Purple200, elevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = currentDay!!.time, modifier = Modifier.padding(
                                top = 8.dp, start = 8.dp
                            ), style = TextStyle(fontSize = 15.sp), color = Color.White
                        )
                        SubcomposeAsyncImage(
                            loading = { CircularProgressIndicator() },
                            model = "https:${currentDay!!.icon}",
                            contentDescription = "im2",
                            modifier = Modifier
                                .padding(
                                    end = 8.dp
                                )
                                .size(35.dp)
                        )
                    }
                    Text(
                        text = currentDay!!.city,
                        style = TextStyle(fontSize = 24.sp),
                        color = Color.White
                    )
                    Text(
                        text = "${currentDay!!.currentTemp}ºC",
                        style = TextStyle(fontSize = 65.sp),
                        color = Color.White
                    )
                    Text(
                        text = currentDay!!.condition,
                        style = TextStyle(fontSize = 16.sp),
                        color = Color.White
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = {
                            dialogState.value = true
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "im3",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "${currentDay!!.maxTemp}ºC/${currentDay!!.minTemp}ºC",
                            style = TextStyle(fontSize = 16.sp),
                            color = Color.White
                        )
                        IconButton(onClick = {
                            if (location != null) {
                                viewModel.updateWeather("${location!!.latitude},${location!!.longitude}")
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_refresh),
                                contentDescription = "im4",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(
                    viewModel.getWeatherByHours(currentDay!!.hours)
                ) { _, item ->
                    MyListItem(item)
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}