package com.example.navyuga.arthYuga.common.network.interfaces

    import com.example.navyuga.arthYuga.common.network.models.ImageUploadResponse
    import okhttp3.MultipartBody
    import retrofit2.Response
    import retrofit2.http.Multipart
    import retrofit2.http.POST
    import retrofit2.http.Part
    import retrofit2.http.Query

    interface ImageUploadApi {
        @Multipart
        @POST("api/1/upload")
        suspend fun uploadImage(
            @Part image: MultipartBody.Part,
            @Query("key") apiKey: String = "6d207e02198a847aa98d0a2a901485a5", // Your Key
            @Query("action") action: String = "upload"
        ): Response<ImageUploadResponse>
    }