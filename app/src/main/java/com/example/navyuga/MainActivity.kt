package com.example.navyuga

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.navyuga.arthYuga.authModule.uis.SignInActivity
import com.example.navyuga.arthYuga.common.utilities.PreferenceManager
import com.example.navyuga.databinding.ActivityMainBinding
import com.example.navyuga.mainModule.uis.*
import com.example.navyuga.arthYuga.searchModule.uis.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentFragmentId = R.id.nav_home // Default

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ⚡ 1. RESTORE STATE logic
        if (savedInstanceState != null) {
            // App is restarting (e.g., Theme Change) -> Restore the last ID
            currentFragmentId = savedInstanceState.getInt("LAST_FRAGMENT_ID", R.id.nav_home)
            binding.bottomNav.selectedItemId = currentFragmentId // This triggers the listener automatically
        } else {
            // First fresh launch -> Load Home
            loadFragment(HomeFragment())
            lockDrawer()
        }

        setupBottomNav()
        setupDrawerNav()
    }

    // ⚡ Save the current screen ID before activity destroys
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("LAST_FRAGMENT_ID", binding.bottomNav.selectedItemId)
    }

    private fun setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    lockDrawer()
                }
                R.id.nav_search -> {
                    loadFragment(SearchFragment())
                    lockDrawer()
                }
                R.id.nav_invest -> {
                    loadFragment(InvestFragment())
                    lockDrawer()
                }
                R.id.nav_reels -> {
                    loadFragment(ReelsFragment())
                    lockDrawer()
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    unlockDrawer() // 🔓 Unlock only for Profile
                }
            }
            true
        }
    }

    private fun setupDrawerNav() {
        // ⚡ 4. DRAWER ACTIONS (Logout, Wallet, etc.)
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    // ⚡ Handle Logout
                    preferenceManager.clear()
                    val intent = Intent(this, SignInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                // Add other drawer actions here (Wallet, Settings, etc.)
            }
            binding.drawerLayout.closeDrawer(GravityCompat.END)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun lockDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun unlockDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
    }
}