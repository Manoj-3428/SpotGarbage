package com.example.spotgarbage.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val BASE_URL="https://detect.roboflow.com/"
    val api: RoboflowApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RoboflowApi::class.java)

    }
}