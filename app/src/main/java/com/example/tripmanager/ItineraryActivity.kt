package com.example.tripmanager

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tripmanager.ui.theme.TripManagerTheme

class ItineraryActivity : ComponentActivity() {
    private var tripId: Int = -1
    private lateinit var tripName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tripId = intent.getIntExtra("tripId", -1)
        tripName = intent.getStringExtra("tripName") ?: "Viaje"

        renderScreen()
    }

    private fun renderScreen() {
        setContent {
            TripManagerTheme {
                ItineraryScreen(
                    tripId = tripId,
                    tripName = tripName,
                    onAddClick = {
                        val intent = Intent(this, AddActivityActivity::class.java)
                        intent.putExtra("tripId", tripId)
                        // 🔹 startActivityForResult para refrescar al regresar
                        startActivityForResult(intent, 1001)
                    }
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            // 🔹 Si se creó una actividad, refrescamos la pantalla
            renderScreen()
        }
    }
}


