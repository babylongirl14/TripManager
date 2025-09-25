package com.example.tripmanager

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(
    tripId: Int,
    tripName: String,
    onAddClick: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val api = RetrofitClient.instance.create(ApiService::class.java)

    var actividades by remember { mutableStateOf<List<ActivityDTO>>(emptyList()) }

    fun loadActivities() {
        val token = sessionManager.fetchAuthToken()
        if (!token.isNullOrEmpty()) {
            api.getActivitiesByTrip("Bearer $token", tripId)
                .enqueue(object : Callback<List<ActivityDTO>> {
                    override fun onResponse(
                        call: Call<List<ActivityDTO>>,
                        response: Response<List<ActivityDTO>>
                    ) {
                        if (response.isSuccessful) {
                            actividades = response.body() ?: emptyList()
                        } else {
                            Toast.makeText(context, "Error cargando actividades", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<ActivityDTO>>, t: Throwable) {
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    LaunchedEffect(tripId) { loadActivities() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Itinerario - $tripName") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick, containerColor = Color(0xFFE0BBFF)) {
                Text("+", color = Color.Black)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (actividades.isEmpty()) {
                Text("No hay actividades todavía", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(actividades) { actividad ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5EFFF))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("📌 ${actividad.descripcion}", style = MaterialTheme.typography.titleMedium)
                                    Text("🕒 ${actividad.hora}")
                                    Text("⚠️ Alerta: ${actividad.alerta}")
                                }

                                IconButton(onClick = {
                                    val token = sessionManager.fetchAuthToken()
                                    if (!token.isNullOrEmpty()) {
                                        api.deleteActivity("Bearer $token", actividad.id)
                                            .enqueue(object : Callback<Void> {
                                                override fun onResponse(
                                                    call: Call<Void>,
                                                    response: Response<Void>
                                                ) {
                                                    if (response.isSuccessful) {
                                                        Toast.makeText(context, "Actividad eliminada", Toast.LENGTH_SHORT).show()
                                                        loadActivities()
                                                    } else {
                                                        Toast.makeText(context, "Error eliminando", Toast.LENGTH_SHORT).show()
                                                    }
                                                }

                                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                                }
                                            })
                                    }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

