package com.example.tripmanager

data class TripDTO(
    val id: Int? = null,
    val destino: String,
    val fecha_inicio: String,
    val fecha_fin: String,
    val tipo: String   // 👈 agregar este campo
)


