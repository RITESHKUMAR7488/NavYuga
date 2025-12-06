package com.example.navyuga.arthYuga.common.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PropertyModel(
    var id: String = "",

    // Basic Info
    val title: String = "",          // e.g., "Property at Park Street"
    val location: String = "",       // e.g., "Kolkata, India"
    val tenantName: String = "",     // e.g., "Reliance", "Tanishq"
    val tenantLogoUrl: String = "",  // URL for the round logo

    // Financials (The Numbers)
    val totalValue: Long = 0,
    val minInvestment: Long = 0,
    val rentAmount: String = "",     // e.g., "6.5 Lakhs"
    val roi: Double = 0.0,           // e.g., 8.5
    val irr: String = "",            // e.g., "12-14%"

    // Status
    val fundedPercentage: Int = 0,   // e.g., 60
    val status: String = "Funding",  // "Funding", "Upcoming", "Exited"

    // Visuals
    val imageUrl: String = ""        // Main property image
) : Parcelable