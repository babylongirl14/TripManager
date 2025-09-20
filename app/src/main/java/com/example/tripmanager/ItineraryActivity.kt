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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tripmanager.ui.theme.TripManagerTheme



data class ActivityItem(val time: String, val desc: String, val alarm: String)

class ItineraryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripManagerTheme {
                ItineraryScreen(
                    onAddClick = {
                        startActivity(Intent(this, AddActivityScreen::class.java))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // 👈 habilita TopAppBar de Material3
@Composable
fun ItineraryScreen(onAddClick: () -> Unit) {
    val activities = listOf(
        ActivityItem(
            "10/julio/2025 7:00am",
            "Vuelo a Madrid, Aeropuerto Internacional de Monterrey, Terminal C",
            "2h antes"
        ),
        ActivityItem(
            "10/julio/2025 12:00pm",
            "Check-in Hotel, Hotel Madrid Centro",
            "30m antes"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Itinerario") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar actividad")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(activities) { activity ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(activity.time, style = MaterialTheme.typography.bodyLarge)
                        Text(activity.desc, style = MaterialTheme.typography.bodyMedium)
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Alarm, contentDescription = "Alarma") // 👈 corregido
                            Text(" ${activity.alarm}")
                        }
                    }
                }
            }
        }
    }
}
