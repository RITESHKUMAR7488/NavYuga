package com.example.estake.searchModule.repositories

import com.example.estake.common.models.PropertyModel
import com.example.estake.common.utilities.UiState

interface SearchRepository {
    suspend fun searchProperties(query: String): UiState<List<PropertyModel>>
}