package com.example.tripmanager

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tripmanager.ui.theme.TripManagerTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripManagerTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Bienvenido a Home")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            startActivity(Intent(this@HomeActivity, MenuActivity::class.java))
                            finish() // cierra Home para no acumular pantallas
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Regresar al Menú")
                    }
                }
            }
        }
    }
}
