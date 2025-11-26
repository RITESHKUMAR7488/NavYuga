package com.example.estake.searchModule.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.estake.common.models.PropertyModel
import com.example.estake.common.utilities.UiState
import com.example.estake.searchModule.repositories.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    // Search Results State
    private val _searchState = MutableStateFlow<UiState<List<PropertyModel>>>(UiState.Idle)
    val searchState: StateFlow<UiState<List<PropertyModel>>> = _searchState

    // Filter States
    val selectedCountry = MutableStateFlow("India")
    val selectedCity = MutableStateFlow("")
    val selectedCurrency = MutableStateFlow("INR")

    // Recent History State
    private val _recentHistory = MutableStateFlow<List<String>>(emptyList())
    val recentHistory: StateFlow<List<String>> = _recentHistory

    init {
        // Dummy History Data
        _recentHistory.value = listOf("Kolkata - Reliance", "Mumbai - HDFC", "Bangalore - Tanishq")
    }

    fun performSearch() {
        val query = "${selectedCity.value} ${selectedCountry.value}"
        viewModelScope.launch {
            _searchState.value = UiState.Loading
            // Use the repository to filter based on the selected city/country
            _searchState.value = repository.searchProperties(query.trim())
        }
    }

    fun updateSelection(country: String, city: String, currency: String) {
        selectedCountry.value = country
        selectedCity.value = city
        selectedCurrency.value = currency
    }
}