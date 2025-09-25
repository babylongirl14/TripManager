package com.example.tripmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.tripmanager.ui.theme.TripManagerTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.compose.LocalLifecycleOwner

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripManagerTheme {
                MenuScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen() {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    var trips by remember { mutableStateOf<List<TripDTO>>(emptyList()) }
    var expandedTrip by remember { mutableStateOf<Int?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var tripToDelete by remember { mutableStateOf<Int?>(null) }

    val api = RetrofitClient.instance.create(ApiService::class.java)

    // 🔹 Función para cargar viajes
    fun cargarViajes() {
        val token = sessionManager.fetchAuthToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(context, "⚠️ No hay sesión activa", Toast.LENGTH_SHORT).show()
            return
        }

        api.getTrips("Bearer $token").enqueue(object : Callback<List<TripDTO>> {
            override fun onResponse(call: Call<List<TripDTO>>, response: Response<List<TripDTO>>) {
                if (response.isSuccessful) {
                    trips = response.body() ?: emptyList()
                } else {
                    Toast.makeText(context, "❌ Error cargando viajes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<TripDTO>>, t: Throwable) {
                Toast.makeText(context, "⚠️ Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 🔹 Recargar cada vez que la pantalla se reanuda
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                cargarViajes()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    context.startActivity(Intent(context, AddTripActivity::class.java))
                },
                containerColor = Color.Black
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar viaje", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Viajes", fontSize = 28.sp)
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Lista de viajes obtenidos
            trips.forEach { trip ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(trip.destino, style = MaterialTheme.typography.titleLarge)

                            // Menú contextual
                            Box {
                                IconButton(onClick = { expandedTrip = trip.id }) {
                                    Icon(Icons.Default.Settings, contentDescription = "Ajustes")
                                }
                                DropdownMenu(
                                    expanded = expandedTrip == trip.id,
                                    onDismissRequest = { expandedTrip = null }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Itinerario") },
                                        onClick = {
                                            expandedTrip = null
                                            val intent = Intent(context, ItineraryActivity::class.java)
                                            intent.putExtra("tripId", trip.id)
                                            intent.putExtra("tripName", trip.destino)
                                            context.startActivity(intent)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Documentos") },
                                        onClick = {
                                            expandedTrip = null
                                            context.startActivity(Intent(context, DocumentsActivity::class.java))
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Eliminar", color = Color.Red) },
                                        onClick = {
                                            expandedTrip = null
                                            tripToDelete = trip.id
                                            showDeleteDialog = true
                                        }
                                    )
                                }
                            }
                        }
                        Divider(Modifier.padding(vertical = 6.dp))
                        Text("Fecha: ${trip.fecha_inicio} - ${trip.fecha_fin}")
                        Text(trip.tipo, color = Color.Gray)
                    }
                }
            }
        }

        // 🔹 Diálogo de confirmación con llamada a la API
        if (showDeleteDialog && tripToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        val token = sessionManager.fetchAuthToken()
                        if (!token.isNullOrEmpty()) {
                            api.deleteTrip("Bearer $token", tripToDelete!!)
                                .enqueue(object : Callback<Void> {
                                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(context, "✅ Viaje eliminado", Toast.LENGTH_SHORT).show()
                                            cargarViajes()
                                        } else {
                                            Toast.makeText(context, "❌ Error al eliminar", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<Void>, t: Throwable) {
                                        Toast.makeText(context, "⚠️ Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                    }
                                })
                        }
                        showDeleteDialog = false
                    }) {
                        Text("Eliminar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("¿Seguro que quieres eliminar el viaje?") }
            )
        }
    }
}
