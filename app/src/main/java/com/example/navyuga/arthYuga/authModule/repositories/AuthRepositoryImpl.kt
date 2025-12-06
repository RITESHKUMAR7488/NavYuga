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

    override suspend fun loginUser(email: String, pass: String): UiState<String> {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            UiState.Success("Login Successful")
        } catch (e: Exception) {
            UiState.Failure(e.localizedMessage ?: "Unknown Error")
        }
    }

    override suspend fun registerUser(userModel: UserModel, pass: String): UiState<String> {
        return try {
            // 1. Create User in Auth
            // We use the email from the model
            val email = userModel.email ?: throw Exception("Email is required")
            auth.createUserWithEmailAndPassword(email, pass).await()

            // 2. Get the User ID
            val userId = auth.currentUser?.uid ?: throw Exception("User creation failed")

            // 3. Update the Model with the ID
            userModel.uid = userId

            // 4. Save UserModel directly to Firestore
            // ⚡ No HashMap needed! Firestore handles the object mapping automatically.
            firestore.collection("users").document(userId).set(userModel).await()

            UiState.Success("Registration Successful")
        } catch (e: Exception) {
            UiState.Failure(e.localizedMessage ?: "Registration Failed")
        }
    }
}