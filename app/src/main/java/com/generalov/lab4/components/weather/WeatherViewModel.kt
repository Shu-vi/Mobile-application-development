package com.generalov.lab4.components.weather

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.generalov.lab4.datastore.PreferencesManager
import com.generalov.lab4.screen.account.registration.RegistrationState
import com.generalov.lab4.types.WeatherData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONArray
import org.json.JSONObject

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val _weatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData> = _weatherData

    private val apiKey = "62179926b8674e4399e82846231405"

    private val preferencesManager: PreferencesManager
    private val fusedLocationClient: FusedLocationProviderClient

    private val _requestState = MutableStateFlow(RequestState.Initial)
    val requestState: StateFlow<RequestState> = _requestState

    init {
        preferencesManager = PreferencesManager(application)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
        val defaultCity: String = preferencesManager.getCity() ?: "Moscow"
        updateWeather(defaultCity)
    }

    fun updateWeather(city: String = "Moscow") {
        _requestState.value = RequestState.Initial
        preferencesManager.saveCity(city)
        val url = "https://api.weatherapi.com/v1/forecast.json?key=$apiKey" +
                "&q=$city" +
                "&days=2" +
                "&aqi=no&alerts=no"
        val queue = Volley.newRequestQueue(getApplication())
        val sRequest = StringRequest(
            com.android.volley.Request.Method.GET,
            url,
            { response ->
                updateWeatherData(response)
                _requestState.value = RequestState.Access
            },
            {
                _requestState.value = RequestState.Error
                Log.d("MyLog", "Ошибка запроса: $it")
            }
        )
        queue.add(sRequest)
    }


    private fun updateWeatherData(response: String) {
        val jsonObject = JSONObject(response)
        val location = jsonObject.getJSONObject("location")
        val current = jsonObject.getJSONObject("current")
        val forecast = jsonObject.getJSONObject("forecast")
        val forecastday = forecast.getJSONArray("forecastday").getJSONObject(0)
        val day = forecastday.getJSONObject("day")
        val hour = forecastday.getJSONArray("hour")

        val city = location.getString("name")
        val time = current.getString("last_updated")
        val currentTemp = current.getString("temp_c")
        val condition = current.getJSONObject("condition").getString("text")
        val icon = current.getJSONObject("condition").getString("icon")
        val maxTemp = day.getString("maxtemp_c")
        val minTemp = day.getString("mintemp_c")

        val hoursList = mutableListOf<JSONObject>()
        for (i in 0 until hour.length()) {
            val conditionLocal = hour.getJSONObject(i).getJSONObject("condition")
            val iconLocal = conditionLocal.getString("icon")
            val text = conditionLocal.getString("text")
            val hourJson = JSONObject()
            hourJson.put("time", hour.getJSONObject(i).getString("time").substring(11..15))
            hourJson.put("temp", hour.getJSONObject(i).getString("temp_c"))
            hourJson.put("icon", iconLocal)
            hourJson.put("condition", text)
            hoursList.add(hourJson)
        }
        val hoursArray = JSONArray(hoursList)
        val hours = hoursArray.toString()


        val weatherData =
            WeatherData(city, time, currentTemp, condition, icon, maxTemp, minTemp, hours)
        _weatherData.value = weatherData
    }

    fun getWeatherByHours(hours: String): List<WeatherData> {
        if (hours.isEmpty()) return listOf()
        val hoursArray = JSONArray(hours)
        val list = ArrayList<WeatherData>()
        for (i in 0 until hoursArray.length()) {
            val item = hoursArray[i] as JSONObject
            list.add(
                WeatherData(
                    "",
                    item.getString("time"),
                    item.getString("temp") + "ºC",
                    item.getString("condition"),
                    item.getString("icon"),
                    "",
                    "",
                    ""
                )
            )
        }
        return list
    }
}

enum class RequestState {
    Access,
    Error,
    Initial
}