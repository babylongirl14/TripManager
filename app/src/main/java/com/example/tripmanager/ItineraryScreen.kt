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
    //LocalContext y remember deben ir DENTRO del cuerpo composable
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val api = remember { RetrofitClient.instance.create(ApiService::class.java) }

    var actividades by remember { mutableStateOf<List<ActivityDTO>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    // Función para cargar actividades desde el backend
    fun loadActivities() {
        if (tripId == -1) {
            actividades = emptyList()
            return
        }
        val token = sessionManager.fetchAuthToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(context, "⚠️ No hay sesión activa", Toast.LENGTH_SHORT).show()
            actividades = emptyList()
            return
        }

        isLoading = true
        api.getActivitiesByTrip("Bearer $token", tripId)
            .enqueue(object : Callback<List<ActivityDTO>> {
                override fun onResponse(call: Call<List<ActivityDTO>>, response: Response<List<ActivityDTO>>) {
                    isLoading = false
                    if (response.isSuccessful) {
                        actividades = response.body() ?: emptyList()
                    } else {
                        Toast.makeText(context, "Error cargando actividades (${response.code()})", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<ActivityDTO>>, t: Throwable) {
                    isLoading = false
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Eliminar actividad por id (recarga lista luego)
    fun deleteActivity(activityId: Int?) {
        val id = activityId ?: run {
            Toast.makeText(context, "ID de actividad inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val token = sessionManager.fetchAuthToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(context, "⚠️ No hay sesión activa", Toast.LENGTH_SHORT).show()
            return
        }

        api.deleteActivity("Bearer $token", id)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Actividad eliminada", Toast.LENGTH_SHORT).show()
                        loadActivities()
                    } else {
                        Toast.makeText(context, "Error eliminando (code ${response.code()})", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Cargar al iniciar y cuando cambie tripId
    LaunchedEffect(tripId) {
        loadActivities()
    }

    Scaffold(
        topBar = {
            // Usamos TopAppBar de material3 (no SmallTopAppBar para evitar incompatibilidades)
            TopAppBar(
                title = { Text(text = "Itinerario - $tripName") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFFE0BBFF)
            ) {
                Text("+", color = Color.Black)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@Column
            }

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
                                    actividad.recordatorio?.let {
                                        Text("⏰ Recordatorio: $it")
                                    }
                                    Text("⚠️ Alerta: ${actividad.alerta}")
                                }

                                IconButton(onClick = { deleteActivity(actividad.id) }) {
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

