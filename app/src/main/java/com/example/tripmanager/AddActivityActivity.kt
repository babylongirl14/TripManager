package com.example.tripmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tripmanager.ui.theme.TripManagerTheme

class AddActivityActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tripId = intent.getIntExtra("tripId", -1)
        val sessionManager = SessionManager(this)
        val api = RetrofitClient.instance.create(ApiService::class.java)

        setContent {
            TripManagerTheme {
                AddActivityScreen(
                    tripId = tripId,
                    sessionManager = sessionManager,
                    api = api,
                    onActivityCreated = {
                        //Avisamos al padre que sí se creó una actividad
                        setResult(RESULT_OK)
                        finish()
                    },
                    onCancel = {
                        finish()
                    }
                )
            }
        }
    }
}
