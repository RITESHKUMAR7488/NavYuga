package com.example.estake.searchModule.repositories

import com.example.estake.common.models.PropertyModel
import com.example.estake.common.utilities.UiState
import kotlinx.coroutines.delay
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor() : SearchRepository {

    // ⚡ Dummy Data (Matches your video)
    private val masterList = listOf(
        PropertyModel(
            title = "Reliance Hub, Park Street",
            rentAmount = "₹ 6.5 Lakhs",
            roi = 8.5,
            location = "Kolkata, West Bengal, India", // Added 'India' for safety
            tenantName = "Reliance",
            imageUrl = "https://upload.wikimedia.org/wikipedia/en/thumb/4/4c/Reliance_Digital_logo.svg/1200px-Reliance_Digital_logo.svg.png"
        ),
        PropertyModel(
            title = "Tanishq Gold Plaza",
            rentAmount = "₹ 12.0 Lakhs",
            roi = 7.2,
            location = "Indiranagar, Bangalore, India",
            tenantName = "Tanishq",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/Tanishq_Logo.svg/2560px-Tanishq_Logo.svg.png"
        ),
        PropertyModel(
            title = "HDFC Financial Tower",
            rentAmount = "₹ 7.8 Lakhs",
            roi = 7.0,
            location = "Mumbai, BKC, India",
            tenantName = "HDFC",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/HDFC_Bank_Logo.svg/2560px-HDFC_Bank_Logo.svg.png"
        ),
        PropertyModel(
            title = "Zudio High Street",
            rentAmount = "₹ 3.8 Lakhs",
            roi = 6.8,
            location = "Delhi, CP",
            tenantName = "Zudio",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Zudio_logo.jpg/800px-Zudio_logo.jpg"
        ),
        PropertyModel(
            title = "Starbucks Cyber Hub",
            rentAmount = "₹ 4.5 Lakhs",
            roi = 6.1,
            location = "Gurugram, Haryana, India",
            tenantName = "Starbucks",
            imageUrl = "https://upload.wikimedia.org/wikipedia/en/thumb/d/d3/Starbucks_Corporation_Logo_2011.svg/1200px-Starbucks_Corporation_Logo_2011.svg.png"
        )
    )

    override suspend fun searchProperties(country: String, city: String): UiState<List<PropertyModel>> {
        delay(300) // Simulate network

        // ⚡ SMART FILTERING LOGIC
        val filteredList = masterList.filter { item ->
            // 1. Check Country (If selected)
            val matchesCountry = if (country.isNotEmpty()) {
                item.location.contains(country, ignoreCase = true)
            } else true

            // 2. Check City (If selected)
            val matchesCity = if (city.isNotEmpty()) {
                item.location.contains(city, ignoreCase = true)
            } else true

            matchesCountry && matchesCity
        }

        return if (filteredList.isNotEmpty()) {
            UiState.Success(filteredList)
        } else {
            UiState.Failure("No properties found in $city")
        }
    }
}