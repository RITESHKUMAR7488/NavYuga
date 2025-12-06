package com.example.navyuga.arthYuga.searchModule.repositories

import com.example.navyuga.arthYuga.common.models.PropertyModel
import com.example.navyuga.arthYuga.common.utilities.UiState

interface SearchRepository {
    // ⚡ Change: Accept separate filters for accuracy
    suspend fun searchProperties(country: String, city: String): UiState<List<PropertyModel>>
}