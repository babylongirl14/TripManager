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

        setContent {
            TripManagerTheme {
                ItineraryScreen(
                    tripId = tripId,
                    tripName = tripName,
                    onAddClick = {
                        val intent = Intent(this, AddActivityActivity::class.java)
                        intent.putExtra("tripId", tripId)
                        startActivity(intent)
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setContent {
            TripManagerTheme {
                ItineraryScreen(
                    tripId = tripId,
                    tripName = tripName,
                    onAddClick = {
                        val intent = Intent(this, AddActivityActivity::class.java)
                        intent.putExtra("tripId", tripId)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

