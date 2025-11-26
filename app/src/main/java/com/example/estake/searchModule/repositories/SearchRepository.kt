package com.example.estake.searchModule.repositories

import com.example.estake.common.models.PropertyModel
import com.example.estake.common.utilities.UiState

interface SearchRepository {
    // ⚡ Change: Accept separate filters for accuracy
    suspend fun searchProperties(country: String, city: String): UiState<List<PropertyModel>>
}