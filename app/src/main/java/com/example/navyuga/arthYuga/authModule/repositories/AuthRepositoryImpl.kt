package com.example.navyuga.arthYuga.authModule.repositories

import com.example.navyuga.arthYuga.authModule.models.UserModel
import com.example.navyuga.arthYuga.common.utilities.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun loginUser(email: String, pass: String): UiState<UserModel> {
        return try {
            // 1. Auth with Firebase
            auth.signInWithEmailAndPassword(email, pass).await()
            val uid = auth.currentUser?.uid ?: throw Exception("UID is null")

            // 2. Fetch User Profile to check Role
            val document = firestore.collection("users").document(uid).get().await()
            val userModel = document.toObject(UserModel::class.java)
                ?: throw Exception("User data not found")

            // 3. Return the full model
            UiState.Success(userModel)

        } catch (e: Exception) {
            UiState.Failure(e.localizedMessage ?: "Unknown Error")
        }
    }

    override suspend fun registerUser(userModel: UserModel, pass: String): UiState<String> {
        // ... (Keep your existing register logic exactly as it is) ...
        return try {
            val email = userModel.email ?: throw Exception("Email is required")
            auth.createUserWithEmailAndPassword(email, pass).await()
            val userId = auth.currentUser?.uid ?: throw Exception("User creation failed")
            userModel.uid = userId
            firestore.collection("users").document(userId).set(userModel).await()
            UiState.Success("Registration Successful")
        } catch (e: Exception) {
            UiState.Failure(e.localizedMessage ?: "Registration Failed")
        }
    }
}