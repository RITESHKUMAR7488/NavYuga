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

    private val _searchState = MutableStateFlow<UiState<List<PropertyModel>>>(UiState.Idle)
    val searchState: StateFlow<UiState<List<PropertyModel>>> = _searchState

    fun onSearchQueryChanged(query: String) {
        viewModelScope.launch {
            _searchState.value = UiState.Loading
            _searchState.value = repository.searchProperties(query)
        }
    }
}