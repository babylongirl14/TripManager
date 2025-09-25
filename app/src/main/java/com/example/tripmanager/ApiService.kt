package com.example.tripmanager

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // 🔹 Login (usa "token/" si configuraste JWT en Django)
    @POST("token/")
    fun login(@Body request: LoginRequest): Call<TokenResponse>

    // 🔹 Obtener actividades de un viaje específico
    @GET("activities/by-trip/{tripId}/")
    fun getActivitiesByTrip(
        @Header("Authorization") authHeader: String,
        @Path("tripId") tripId: Int
    ): Call<List<ActivityDTO>>

    // 🔹 Crear nueva actividad
    @POST("activities/")
    fun createActivity(
        @Header("Authorization") authHeader: String,
        @Body activity: ActivityDTO
    ): Call<ActivityDTO>

    // 🔹 Obtener viajes del usuario autenticado
    @GET("trips/")
    fun getTrips(
        @Header("Authorization") authHeader: String
    ): Call<List<TripDTO>>

    // 🔹 Crear un nuevo viaje
    @POST("trips/")
    fun createTrip(
        @Header("Authorization") authHeader: String,
        @Body trip: TripDTO
    ): Call<TripDTO>

    // 🔹 Eliminar actividad
    @DELETE("activities/{id}/")
    fun deleteActivity(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int
    ): Call<Void>

    @PATCH("activities/{id}/")
    fun updateActivity(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body actividad: ActivityDTO
    ): Call<ActivityDTO>


}
