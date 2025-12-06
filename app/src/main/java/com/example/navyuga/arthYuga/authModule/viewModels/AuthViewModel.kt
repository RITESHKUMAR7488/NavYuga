package com.example.navyuga.arthYuga.authModule.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navyuga.arthYuga.authModule.models.UserModel
import com.example.navyuga.arthYuga.authModule.repositories.AuthRepository
import com.example.navyuga.arthYuga.common.utilities.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val loginState: StateFlow<UiState<String>> = _loginState

    private val _registerState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val registerState: StateFlow<UiState<String>> = _registerState

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _loginState.value = UiState.Loading
            val result = repository.loginUser(email, pass)
            _loginState.value = result
        }
    }

    fun register(email: String, pass: String, name: String) {
        viewModelScope.launch {
            _registerState.value = UiState.Loading

            // ⚡ Create the UserModel here
            val newUser = UserModel(
                email = email,
                name = name,
                role = "user"
            )

            val result = repository.registerUser(newUser, pass)
            _registerState.value = result
        }
    }
}