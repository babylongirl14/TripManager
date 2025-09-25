package com.example.tripmanager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.tripmanager.ui.screens.RegisterScreen
import com.example.tripmanager.viewModel.UserViewModel

class RegisterActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterScreen(userViewModel)
        }
    }
}
