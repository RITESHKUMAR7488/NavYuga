package com.example.navyuga.arthYuga.adminModule.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navyuga.arthYuga.adminModule.repositories.AdminRepository
import com.example.navyuga.arthYuga.authModule.models.UserModel
import com.example.navyuga.arthYuga.common.utilities.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    private val _createState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val createState: StateFlow<UiState<String>> = _createState

    fun createAccount(name: String, email: String, pass: String, role: String) {
        viewModelScope.launch {
            _createState.value = UiState.Loading

            val newUser = UserModel(
                name = name,
                email = email,
                role = role.lowercase(), // "admin" or "user"
                totalInvestment = 0,
                currentValue = 0
            )

            _createState.value = repository.createUser(newUser, pass)
        }
    }
}