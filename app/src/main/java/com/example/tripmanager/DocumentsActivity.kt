package com.example.tripmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import com.example.tripmanager.ui.theme.TripManagerTheme

class DocumentsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripManagerTheme {
                DocumentsScreen()
            }
        }
    }
}

@Composable
fun DocumentsScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Text(text = "Aquí van tus documentos", style = MaterialTheme.typography.headlineMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun DocumentsPreview() {
    TripManagerTheme {
        DocumentsScreen()
    }
}
