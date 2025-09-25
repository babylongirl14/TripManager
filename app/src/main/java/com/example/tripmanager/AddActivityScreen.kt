package com.example.tripmanager

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(
    tripId: Int,
    sessionManager: SessionManager,
    api: ApiService,
    onActivityCreated: () -> Unit, //callback que refresca Itinerary
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    var descripcion by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var alerta by remember { mutableStateOf("Normal") }
    var loading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Actividad") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora (ej. 10:00)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción de actividad") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Tipo de alerta", fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = alerta == "Normal",
                    onClick = { alerta = "Normal" }
                )
                Text("Normal")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = alerta == "Importante",
                    onClick = { alerta = "Importante" }
                )
                Text("Importante")
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (loading) {
                CircularProgressIndicator()
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onCancel,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            if (descripcion.isNotEmpty() && hora.isNotEmpty()) {
                                val token = sessionManager.fetchAuthToken()
                                if (!token.isNullOrEmpty()) {
                                    loading = true
                                    val horaFormateada = if (hora.length == 5) "$hora:00" else hora

                                    val nuevaActividad = ActivityDTO(
                                        trip = tripId,
                                        descripcion = descripcion,
                                        hora = horaFormateada,
                                        alerta = alerta
                                    )

                                    api.createActivity("Bearer $token", nuevaActividad)
                                        .enqueue(object : Callback<ActivityDTO> {
                                            override fun onResponse(
                                                call: Call<ActivityDTO>,
                                                response: Response<ActivityDTO>
                                            ) {
                                                loading = false
                                                if (response.isSuccessful) {
                                                    Toast.makeText(
                                                        context,
                                                        "✅ Actividad creada",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    onActivityCreated() //refresca Itinerary
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "❌ Error al crear actividad",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<ActivityDTO>,
                                                t: Throwable
                                            ) {
                                                loading = false
                                                Toast.makeText(
                                                    context,
                                                    "⚠️ Error: ${t.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        })
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Por favor completa todos los campos",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE0BBFF),
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}
