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

class AddTripActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripManagerTheme {
                AddTripScreen { destino, fecha, tipo ->
                    Toast.makeText(
                        this,
                        "Viaje a $destino agregado",
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
fun AddTripScreen(onSave: (String, String, String) -> Unit) {
    val context = LocalContext.current

    var destino by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Viajes",
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
                        text = "Nuevo viaje",
                        fontSize = 28.sp,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(text = "Destino", fontSize = 18.sp, color = Color.Black)
                    OutlinedTextField(
                        value = destino,
                        onValueChange = { destino = it },
                        placeholder = { Text("Nombre del destino") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Text(text = "Inicio y Fin", fontSize = 18.sp, color = Color.Black)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = fechaInicio,
                            onValueChange = { fechaInicio = it },
                            placeholder = { Text("00/00/0000") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = fechaFin,
                            onValueChange = { fechaFin = it },
                            placeholder = { Text("00/00/0000") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Text(text = "Tipo", fontSize = 18.sp, color = Color.Black)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        OutlinedButton(
                            onClick = { tipo = "Vacaciones" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            border = if (tipo == "Vacaciones")
                                ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
                            else
                                ButtonDefaults.outlinedButtonBorder
                        ) {
                            Text(
                                text = "Vacaciones",
                                color = if (tipo == "Vacaciones") Color(0xFF007BFF) else Color.Gray
                            )
                        }

                        OutlinedButton(
                            onClick = { tipo = "Trabajo" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            border = if (tipo == "Trabajo")
                                ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
                            else
                                ButtonDefaults.outlinedButtonBorder
                        ) {
                            Text(
                                text = "Trabajo",
                                color = if (tipo == "Trabajo") Color(0xFF007BFF) else Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (destino.isNotBlank() && fechaInicio.isNotBlank() && fechaFin.isNotBlank() && tipo.isNotBlank()) {
                                onSave(destino, "$fechaInicio - $fechaFin", tipo)
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
                        Text(text = "Guardar", color = Color.White, fontSize = 18.sp)
                    }

                }
            }
        }
    }
}


