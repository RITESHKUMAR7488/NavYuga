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
import com.example.navyuga.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeState()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() } // Go back to Login

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ⚡ Call the Coroutine function
            viewModel.register(email, pass, name)
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.registerState.collect { state ->
                when (state) {
                    // ⚡ ADD THIS BLOCK
                    is UiState.Idle -> {
                        binding.btnRegister.text = "Create Account"
                        binding.btnRegister.isEnabled = true
                    }
                    is UiState.Loading -> {
                        binding.btnRegister.text = "Creating Account..."
                        binding.btnRegister.isEnabled = false
                    }
                    is UiState.Success -> {
                        binding.btnRegister.text = "Success"
                        // Save Session
                        preferenceManager.putBoolean(PreferenceManager.KEY_IS_LOGGED_IN, true)

                        Toast.makeText(this@RegisterActivity, "Welcome!", Toast.LENGTH_SHORT).show()

                        // Go to Main Dashboard
                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    is UiState.Failure -> {
                        binding.btnRegister.text = "Create Account"
                        binding.btnRegister.isEnabled = true
                        Toast.makeText(this@RegisterActivity, state.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}