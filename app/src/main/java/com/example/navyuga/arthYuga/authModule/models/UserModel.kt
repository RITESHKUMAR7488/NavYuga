package com.example.navyuga.arthYuga.authModule.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var uid: String? = null,
    var email: String? = null,
    var name: String? = null,
    var role: String? = "user", // "user" or "admin"
    var mobileNumber: String? = null,
    var profilePicUrl: String? = "",
    var totalInvestment: Long = 0,
    var currentValue: Long = 0
) : Parcelable