package com.example.weatherapp

class WeatherRepository(private val api: WeatherApi) {
    suspend fun getWeather(cityQuery: String, apiKey: String): WeatherResponse {
        return api.getWeatherByCity(cityQuery, apiKey)
    }
}
