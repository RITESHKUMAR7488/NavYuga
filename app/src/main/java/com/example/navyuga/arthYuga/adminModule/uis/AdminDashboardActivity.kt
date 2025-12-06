package com.example.navyuga.arthYuga.adminModule.uis

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.navyuga.arthYuga.authModule.uis.SignInActivity
import com.example.navyuga.arthYuga.common.utilities.PreferenceManager
import com.example.navyuga.databinding.ActivityAdminDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddProperty.setOnClickListener {
            // TODO: Go to AddPropertyActivity
        }

        binding.btnAddInvestment.setOnClickListener {
            // TODO: Go to RegisterInvestmentActivity
        }

        binding.btnLogout.setOnClickListener {
            preferenceManager.clear()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
        binding.btnAddUser.setOnClickListener {
            startActivity(Intent(this, AddUserActivity::class.java))
        }
    }
}