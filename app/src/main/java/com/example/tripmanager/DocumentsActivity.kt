//no funciona aun

package com.example.tripmanager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tripmanager.ui.theme.TripManagerTheme

class DocumentsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tripName = intent.getStringExtra("tripName") ?: "Viaje"

        setContent {
            TripManagerTheme {
                DocumentsScreen(tripName = tripName)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsScreen(tripName: String) {
    val context = LocalContext.current

    val documentos = remember { mutableStateListOf("Pasaporte.pdf", "BoletoVuelo.pdf") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Documentos - $tripName") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    documentos.add("NuevoDocumento_${documentos.size + 1}.pdf")
                    Toast.makeText(context, "Documento agregado", Toast.LENGTH_SHORT).show()
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar documento")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(documentos) { doc ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Icon(Icons.Default.Description, contentDescription = "Doc")
                            Spacer(Modifier.width(8.dp))
                            Text(doc)
                        }
                        TextButton(onClick = {
                            documentos.remove(doc)
                            Toast.makeText(context, "Documento eliminado", Toast.LENGTH_SHORT).show()
                        }) {
                            Text("Eliminar", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
