package com.example.navyuga.arthYuga.adminModule.repositories

import android.content.Context
import com.example.navyuga.arthYuga.authModule.models.UserModel
import com.example.navyuga.arthYuga.common.utilities.UiState
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AdminRepository {
    suspend fun createUser(user: UserModel, password: String): UiState<String>
}

class AdminRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    // Context is needed to initialize the secondary app
    private val context: Context
) : AdminRepository {

    override suspend fun createUser(user: UserModel, password: String): UiState<String> {
        return try {
            // ⚡ 1. Initialize a Secondary Firebase App ("Ghost")
            // We reuse the options from the default app so it connects to the same project
            val options = FirebaseApp.getInstance().options
            val secondaryAppName = "SecondaryApp"

            // Check if it already exists to avoid crash
            var secondaryApp = try {
                FirebaseApp.getInstance(secondaryAppName)
            } catch (e: Exception) {
                FirebaseApp.initializeApp(context, options, secondaryAppName)
            }

            // ⚡ 2. Get Auth instance for the Ghost App
            val secondaryAuth = FirebaseAuth.getInstance(secondaryApp)

            // ⚡ 3. Create the User (This won't affect the Main Admin's session)
            secondaryAuth.createUserWithEmailAndPassword(user.email!!, password).await()
            val newUserId = secondaryAuth.currentUser?.uid ?: throw Exception("ID generation failed")

            // ⚡ 4. Save Profile to Firestore (Using Main Admin's Firestore instance)
            user.uid = newUserId
            firestore.collection("users").document(newUserId).set(user).await()

            // ⚡ 5. Cleanup: Sign out the ghost user so the instance is clean
            secondaryAuth.signOut()

            UiState.Success("User created successfully: ${user.email}")

        } catch (e: Exception) {
            UiState.Failure(e.localizedMessage ?: "Failed to create user")
        }
    }
}