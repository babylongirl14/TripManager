package com.example.tripmanager

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripmanager.ui.theme.TripManagerTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Settings



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

    //Aquí después se cargará desde Django, se supone que aqui se conecta la db
    val trips = remember {
        mutableStateListOf(
            Triple("Madrid", "10/julio/2025", "Vacaciones"),
            Triple("Japón", "14/octubre/2025", "Vacaciones"),
            Triple("CDMX", "20/septiembre/2025", "Trabajo")
        )
    }

    var expandedTrip by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var tripToDelete by remember { mutableStateOf<String?>(null) }

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

            // Lista de viajes
            trips.forEach { (destino, fecha, tipo) ->
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
                            Text(destino, style = MaterialTheme.typography.titleLarge)

                            // Menú contextual
                            Box {
                                IconButton(onClick = { expandedTrip = destino }) {
                                    Icon(Icons.Default.Settings, contentDescription = "Ajustes")

                                }
                                DropdownMenu(
                                    expanded = expandedTrip == destino,
                                    onDismissRequest = { expandedTrip = null }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Itinerario") },
                                        onClick = {
                                            expandedTrip = null
                                            context.startActivity(Intent(context, ItineraryActivity::class.java))
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
                                            tripToDelete = destino
                                            showDeleteDialog = true
                                        }
                                    )
                                }
                            }
                        }
                        Divider(Modifier.padding(vertical = 6.dp))
                        Text("Fecha: $fecha", style = MaterialTheme.typography.bodyMedium)
                        Text(tipo, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
            }
        }

        // Diálogo de confirmación
        if (showDeleteDialog && tripToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        // 👉 Aquí después conectas con Django para eliminar viaje
                        trips.removeIf { it.first == tripToDelete }
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

