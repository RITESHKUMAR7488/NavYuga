package com.example.navyuga.arthYuga.common.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageUploadResponse(
    @SerializedName("status_code") @Expose val statusCode: Int? = null,
    @SerializedName("success") @Expose val success: Success? = null,
    @SerializedName("image") @Expose val image: Image? = null,
    @SerializedName("status_txt") @Expose val statusText: String? = null
)

data class Success(
    @SerializedName("message") @Expose val message: String? = null,
    @SerializedName("code") @Expose val code: Int? = null
)

data class Image(
    @SerializedName("name") @Expose val name: String? = null,
    @SerializedName("url") @Expose val url: String? = null,
    @SerializedName("display_url") @Expose val displayUrl: String? = null,
    @SerializedName("thumb") @Expose val thumb: Thumb? = null,
    @SerializedName("medium") @Expose val medium: Medium? = null
)

data class Thumb(
    @SerializedName("url") @Expose val url: String? = null
)

data class Medium(
    @SerializedName("url") @Expose val url: String? = null
)