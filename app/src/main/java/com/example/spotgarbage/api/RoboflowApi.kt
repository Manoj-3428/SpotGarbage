package com.example.spotgarbage.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface RoboflowApi {
    @Multipart
    @POST("garbage_detection-wvzwv/9")
    suspend fun detectGarbage(
        @Query("api_key") api:String,
        @Part image:MultipartBody.Part
    ):Response<DetectionResponse>
}
//     waste-segregation-paper-glass-plastic/1 chocolate-detection-41qji/1
