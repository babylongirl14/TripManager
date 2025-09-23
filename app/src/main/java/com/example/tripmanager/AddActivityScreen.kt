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

class AddActivityScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tripName = intent.getStringExtra("tripName") ?: "Viaje"

        setContent {
            TripManagerTheme {
                AddActivityContent(tripName) { descripcion, recordatorio, tiempo, alerta ->
                    Toast.makeText(
                        this,
                        "Actividad agregada a $tripName",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityContent(
    tripName: String,
    onSave: (String, Boolean, String, String) -> Unit
) {
    val context = LocalContext.current

    var descripcion by remember { mutableStateOf("") }
    var recordatorio by remember { mutableStateOf(false) }
    var tiempoRecordatorio by remember { mutableStateOf("") }
    var tipoAlerta by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Itinerario",
                        fontSize = 28.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
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
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF2F2F2)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Nueva actividad",
                        fontSize = 24.sp,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Divider(color = Color.Gray, thickness = 1.dp)

                    Text("Descripción de actividad", fontSize = 18.sp, color = Color.Black)
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        placeholder = { Text("Ej. Vuelo a Madrid...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = false
                    )

                    Text("Agregar recordatorio", fontSize = 18.sp, color = Color.Black)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = recordatorio,
                            onClick = { recordatorio = true }
                        )
                        Text("Sí")
                        RadioButton(
                            selected = !recordatorio,
                            onClick = { recordatorio = false }
                        )
                        Text("No")
                    }

                    if (recordatorio) {
                        OutlinedTextField(
                            value = tiempoRecordatorio,
                            onValueChange = { tiempoRecordatorio = it },
                            placeholder = { Text("Ej. 2h 30min") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Text("Tipo de alerta", fontSize = 18.sp, color = Color.Black)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        OutlinedButton(
                            onClick = { tipoAlerta = "Normal" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            border = if (tipoAlerta == "Normal")
                                ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
                            else
                                ButtonDefaults.outlinedButtonBorder
                        ) {
                            Text(
                                text = "Normal",
                                color = if (tipoAlerta == "Normal") Color(0xFF007BFF) else Color.Gray
                            )
                        }

                        OutlinedButton(
                            onClick = { tipoAlerta = "Importante" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            border = if (tipoAlerta == "Importante")
                                ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
                            else
                                ButtonDefaults.outlinedButtonBorder
                        ) {
                            Text(
                                text = "Importante",
                                color = if (tipoAlerta == "Importante") Color(0xFF007BFF) else Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (descripcion.isNotBlank() && tipoAlerta.isNotBlank()) {
                                onSave(descripcion, recordatorio, tiempoRecordatorio, tipoAlerta)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Completa todos los campos",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
                    ) {
                        Text("Guardar", color = Color.White, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

