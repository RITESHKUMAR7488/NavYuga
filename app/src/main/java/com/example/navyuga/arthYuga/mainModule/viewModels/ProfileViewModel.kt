package com.example.navyuga.arthYuga.mainModule.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navyuga.arthYuga.common.utilities.UiState
import com.example.navyuga.arthYuga.mainModule.adapters.PortfolioStat
import com.example.navyuga.arthYuga.mainModule.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    // State for the Horizontal Stats List
    private val _statsState = MutableStateFlow<UiState<List<PortfolioStat>>>(UiState.Loading)
    val statsState: StateFlow<UiState<List<PortfolioStat>>> = _statsState

    // State for User Name/Email
    private val _userState = MutableStateFlow<UiState<Map<String, String>>>(UiState.Loading)
    val userState: StateFlow<UiState<Map<String, String>>> = _userState

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        viewModelScope.launch {
            _statsState.value = repository.getPortfolioStats()
            _userState.value = repository.getUserProfile()
        }
    }
}