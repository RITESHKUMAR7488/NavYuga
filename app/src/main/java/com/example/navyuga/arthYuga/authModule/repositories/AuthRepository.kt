package com.example.navyuga.arthYuga.authModule.repositories

import com.example.navyuga.arthYuga.authModule.models.UserModel
import com.example.navyuga.arthYuga.common.utilities.UiState

interface AuthRepository {
    suspend fun loginUser(email: String, pass: String): UiState<String>
    // ⚡ Now accepts UserModel instead of just strings
    suspend fun registerUser(userModel: UserModel, pass: String): UiState<String>
}