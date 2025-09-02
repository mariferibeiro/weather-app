package com.example.weatherapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val API_KEY = "0fb1e2afa0cd97bb072f8abb7e5b432f"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editCidade = findViewById<EditText>(R.id.editCidade)
        val editEstado = findViewById<EditText>(R.id.editEstado)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val textResultado = findViewById<TextView>(R.id.textResultado)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(WeatherApi::class.java)
        val repository = WeatherRepository(api)

        btnBuscar.setOnClickListener {
            val cidade = editCidade.text.toString().trim()
            val estado = editEstado.text.toString().trim()

            if (cidade.isEmpty()) {
                textResultado.text = "Por favor, digite a cidade."
                return@setOnClickListener
            }

            val cidadeCompleta = if (estado.isNotEmpty()) "$cidade,$estado,BR" else "$cidade,BR"

            lifecycleScope.launch {
                try {
                    val clima = repository.getWeather(cidadeCompleta, API_KEY)
                    val temperatura = clima.main?.temp
                    val descricao = clima.weather?.firstOrNull()?.description
                    val cidadeNome = clima.name

                    textResultado.text = "Cidade: $cidadeNome\nTemperatura: ${temperatura}°C\nDescrição: $descricao"
                } catch (e: Exception) {
                    textResultado.text = "Erro: ${e.message}"
                }
            }
        }
    }
}
