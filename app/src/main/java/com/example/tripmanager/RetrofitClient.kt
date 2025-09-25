package com.example.tripmanager

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/api/"  // Android Emulator -> localhost

    // 🔹 Retrofit se guarda en "retrofit" y no en "instance" para evitar confusiones
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 🔹 Aquí exponemos "instance" como Retrofit listo
    val instance: Retrofit
        get() = retrofit
}

