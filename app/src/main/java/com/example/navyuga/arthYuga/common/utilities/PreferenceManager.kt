package com.example.navyuga.arthYuga.common.utilities

import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    companion object {
        const val KEY_USER_ID = "user_id"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val KEY_IS_DARK_MODE = "is_dark_mode"
    }
}