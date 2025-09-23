package com.example.tripmanager

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripmanager.ui.theme.TripManagerTheme

data class ActivityItem(
    val tripName: String,
    var time: String,
    var desc: String,
    var reminder: String,
    var alert: String
)

class ItineraryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tripName = intent.getStringExtra("tripName") ?: "Viaje"

        setContent {
            TripManagerTheme {
                ItineraryScreen(
                    tripName = tripName,
                    onAddClick = {
                        val intent = Intent(this, AddActivityScreen::class.java)
                        intent.putExtra("tripName", tripName)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun ItineraryScreen(
    tripName: String,
    onAddClick: () -> Unit
) {
    // Lista simulada (después se conecta a la BD)
    val activities = remember {
        mutableStateListOf(
            ActivityItem(tripName, "10/julio/2025 7:00am", "Vuelo a $tripName, Aeropuerto Internacional de Monterrey, Terminal C", "2h antes", "Importante"),
            ActivityItem(tripName, "10/julio/2025 12:00pm", "Check-in Hotel Madrid Centro", "30m antes", "Importante"),
            ActivityItem(tripName, "10/julio/2025 2:00pm", "Comer en restaurante “Sobrino de Botín”", "-", "Normal"),
            ActivityItem(tripName, "10/julio/2025 5:00pm", "Visitar Museo del Prado", "1h antes", "Normal")
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar actividad")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(tripName, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text("Itinerario", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }

                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(40.dp),
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(activities) { activity ->
                    ActivityCard(
                        activity = activity,
                        onEdit = { updated ->
                            val index = activities.indexOf(activity)
                            if (index != -1) {
                                activities[index] = updated
                            }
                        },
                        onDelete = { toDelete ->
                            activities.remove(toDelete)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityCard(
    activity: ActivityItem,
    onEdit: (ActivityItem) -> Unit,
    onDelete: (ActivityItem) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    var desc by remember { mutableStateOf(activity.desc) }
    var alert by remember { mutableStateOf(activity.alert) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(activity.time, fontWeight = FontWeight.SemiBold)
                }

                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Opciones de actividad"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            onClick = {
                                expanded = false
                                showEditDialog = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar", color = MaterialTheme.colorScheme.error) },
                            onClick = {
                                expanded = false
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(activity.desc)
            Spacer(modifier = Modifier.height(4.dp))
            Text("⏰ ${activity.reminder}", fontSize = 12.sp)
            Text("Alerta: ${activity.alert}", fontSize = 12.sp)
        }
    }

    // es ek diálogo de confirmación para eliminar
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(activity) // acción de borrado real
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
            title = { Text("¿Seguro que quieres eliminar la actividad?") }
        )
    }

    // es el formulario para editar actividad
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val updated = activity.copy(desc = desc, alert = alert)
                    onEdit(updated)
                    showEditDialog = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Editar actividad") },
            text = {
                Column {
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Descripción de actividad") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Tipo de alerta:")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = alert == "Normal",
                            onClick = { alert = "Normal" }
                        )
                        Text("Normal")
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = alert == "Importante",
                            onClick = { alert = "Importante" }
                        )
                        Text("Importante")
                    }
                }
            }
        )
    }
}
