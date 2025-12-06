package com.example.navyuga.arthYuga.authModule.uis

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.navyuga.MainActivity
import com.example.navyuga.arthYuga.authModule.viewModels.AuthViewModel
import com.example.navyuga.arthYuga.common.utilities.PreferenceManager
import com.example.navyuga.arthYuga.common.utilities.UiState
import com.example.navyuga.databinding.ActivitySignInBinding
import androidx.appcompat.app.AppCompatDelegate
import com.example.navyuga.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    // ⚡ Hilt automatically injects the ViewModel factory
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Check if already logged in
        if (preferenceManager.getBoolean(PreferenceManager.KEY_IS_LOGGED_IN)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupThemeToggle()
        setupListeners()
        observeState()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ⚡ Trigger the Coroutine in ViewModel
            viewModel.login(email, pass)
        }

        binding.tvRegister.setOnClickListener {
            binding.tvRegister.setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.btnLogin.text = "Verifying..."
                        binding.btnLogin.isEnabled = false
                    }
                    is UiState.Success -> {
                        binding.btnLogin.text = "Success"
                        preferenceManager.putBoolean(PreferenceManager.KEY_IS_LOGGED_IN, true)

                        // ⚡ CHECK ROLE
                        val user = state.data
                        if (user.role == "admin") {
                            // Go to Admin Dashboard
                            startActivity(Intent(this@SignInActivity, com.example.navyuga.arthYuga.adminModule.uis.AdminDashboardActivity::class.java))
                        } else {
                            // Go to User App
                            startActivity(Intent(this@SignInActivity, com.example.navyuga.MainActivity::class.java))
                        }
                        finish()
                    }
                    is UiState.Failure -> {
                        binding.btnLogin.text = "Secure Login"
                        binding.btnLogin.isEnabled = true
                        Toast.makeText(this@SignInActivity, state.error, Toast.LENGTH_LONG).show()
                    }
                    else -> {}
                }
            }
        }
    }
    private fun setupThemeToggle() {
        // 1. Check current state
        val isDark = preferenceManager.getBoolean(PreferenceManager.KEY_IS_DARK_MODE)
        updateThemeIcon(isDark)

        // 2. Handle Click
        binding.btnThemeToggle.setOnClickListener {
            val currentMode = preferenceManager.getBoolean(PreferenceManager.KEY_IS_DARK_MODE)
            val newMode = !currentMode

            // Save new preference
            preferenceManager.putBoolean(PreferenceManager.KEY_IS_DARK_MODE, newMode)

            // Apply Theme (This will recreate the Activity automatically)
            if (newMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            updateThemeIcon(newMode)
        }
    }

    private fun updateThemeIcon(isDark: Boolean) {
        if (isDark) {
            binding.btnThemeToggle.setImageResource(R.drawable.ic_sun) // Show Sun to switch to Light
        } else {
            binding.btnThemeToggle.setImageResource(R.drawable.ic_moon) // Show Moon to switch to Dark
        }
    }
}