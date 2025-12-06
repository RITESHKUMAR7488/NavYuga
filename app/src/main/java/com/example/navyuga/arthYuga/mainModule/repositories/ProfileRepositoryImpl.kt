package com.example.navyuga.arthYuga.mainModule.repositories

import com.example.navyuga.arthYuga.common.utilities.UiState
import com.example.navyuga.arthYuga.mainModule.adapters.PortfolioStat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ProfileRepository {

    override suspend fun getPortfolioStats(): UiState<List<PortfolioStat>> {
        // ⚡ SIMULATING NETWORK DELAY (So you see it loading like a real app)
        delay(500)

        // ⚡ DUMMY DATA: This mimics what the Admin will eventually add to Firestore
        val dummyStats = listOf(
            PortfolioStat("My Properties", "12", 75, "#00E5FF"),       // Cyan
            PortfolioStat("Total Annual Rent", "₹3,63,72,612", 60, "#2979FF"), // Blue
            PortfolioStat("Total Sq Ft", "38,400", 85, "#FFC107"),    // Gold
            PortfolioStat("Total P/L", "₹12,36,27,963", 90, "#D500F9"), // Purple
            PortfolioStat("Avg ROI", "9.2%", 45, "#2979FF")           // Blue
        )

        return UiState.Success(dummyStats)
    }

    override suspend fun getUserProfile(): UiState<Map<String, String>> {
        val user = auth.currentUser
        return if (user != null) {
            val profileData = mapOf(
                "name" to (user.displayName ?: "Valued Investor"),
                "email" to (user.email ?: "investor@estake.com")
            )
            UiState.Success(profileData)
        } else {
            UiState.Failure("User not logged in")
        }
    }
}