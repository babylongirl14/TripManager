package com.example.tripmanager.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripmanager.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel : ViewModel() {

    // Lista simple de usuarios (simula un repositorio)
    private val users = mutableListOf<User>()

    // Registrar usuario
    fun registerUser(user: User, onResult: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                users.add(user)
            }
            onResult()
        }
    }

    // Login
    fun login(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val userFound = withContext(Dispatchers.IO) {
                users.any { it.username == username && it.passwordHash == password }
            }
            onResult(userFound)
        }
    }
}
