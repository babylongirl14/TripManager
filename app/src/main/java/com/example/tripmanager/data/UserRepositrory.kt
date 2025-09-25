package com.example.tripmanager.data

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: User): Boolean {
        val existingUser = userDao.getUserByUsername(user.username)
        return if (existingUser == null) {
            userDao.insert(user)
            true
        } else {
            false
        }
    }

    suspend fun login(username: String, password: String): Boolean {
        val user = userDao.getUserByUsername(username)
        return user?.passwordHash == password
    }
}
