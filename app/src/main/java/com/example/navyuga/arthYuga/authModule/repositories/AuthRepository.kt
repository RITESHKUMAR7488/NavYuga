package com.example.navyuga.arthYuga.authModule.repositories

import com.example.navyuga.arthYuga.authModule.models.UserModel
import com.example.navyuga.arthYuga.common.utilities.UiState

interface AuthRepository {
    // ⚡ Changed return type to UserModel
    suspend fun loginUser(email: String, pass: String): UiState<UserModel>
    suspend fun registerUser(userModel: UserModel, pass: String): UiState<String>
}