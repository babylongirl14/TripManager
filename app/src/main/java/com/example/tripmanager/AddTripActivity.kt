package com.example.tripmanager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripmanager.ui.theme.TripManagerTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTripActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripManagerTheme {
                AddTripScreen { trip ->
                    // Llamada a la API para guardar
                    val api = RetrofitClient.instance.create(ApiService::class.java)
                    val sessionManager = SessionManager(this)
                    val token = sessionManager.fetchAuthToken()

                    if (token == null) {
                        Toast.makeText(this, "⚠️ Debes iniciar sesión", Toast.LENGTH_SHORT).show()
                        return@AddTripScreen
                    }

                    api.createTrip("Bearer $token", trip)
                        .enqueue(object : Callback<TripDTO> {
                            override fun onResponse(
                                call: Call<TripDTO>,
                                response: Response<TripDTO>
                            ) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        this@AddTripActivity,
                                        "✅ Viaje agregado: ${response.body()?.destino}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@AddTripActivity,
                                        "❌ Error al crear viaje",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<TripDTO>, t: Throwable) {
                                Toast.makeText(
                                    this@AddTripActivity,
                                    "⚠️ Error: ${t.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTripScreen(onSave: (TripDTO) -> Unit) {
    val context = LocalContext.current

    var destino by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Viaje", fontSize = 24.sp) },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .size(40.dp),
                        tint = Color.Unspecified
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = destino,
                onValueChange = { destino = it },
                label = { Text("Destino") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = fechaInicio,
                onValueChange = { fechaInicio = it },
                label = { Text("Fecha inicio (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = fechaFin,
                onValueChange = { fechaFin = it },
                label = { Text("Fecha fin (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo (Vacaciones/Trabajo)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (destino.isNotBlank() && fechaInicio.isNotBlank() &&
                        fechaFin.isNotBlank() && tipo.isNotBlank()
                    ) {
                        val trip = TripDTO(
                            destino = destino,
                            fecha_inicio = fechaInicio,
                            fecha_fin = fechaFin,
                            tipo = tipo
                        )
                        onSave(trip)
                    } else {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
            ) {
                Text("Guardar", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}
