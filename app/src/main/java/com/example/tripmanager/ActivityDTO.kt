package com.example.tripmanager

import com.google.gson.annotations.SerializedName

data class ActivityDTO(
    val id: Int? = null,
    val trip: Int,
    val descripcion: String,
    val hora: String,
    val alerta: String,
    val recordatorio: String? = null
)

