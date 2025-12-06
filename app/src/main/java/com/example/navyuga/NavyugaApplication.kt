package com.example.navyuga

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.navyuga.arthYuga.common.utilities.PreferenceManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NavyugaApplication : Application() {

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate() {
        super.onCreate()
        setupTheme()
    }

    private fun setupTheme() {
        val key = PreferenceManager.KEY_IS_DARK_MODE

        if (preferenceManager.contains(key)) {
            // 1. User has manually chosen a theme before
            val isDark = preferenceManager.getBoolean(key)
            if (isDark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        } else {
            // 2. First launch (or no preference) -> Follow Phone Settings
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}