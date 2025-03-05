package com.example.spotgarbage.api

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class GarbageDetectionRepository {
    suspend fun detectGarbage(filepath: String,apiKey:String): DetectionResponse?{
        val imagePart=prepareImagePart(filepath)
        val response= RetrofitClient.api.detectGarbage(apiKey,imagePart)
        if(response.isSuccessful){
            return response.body()
        }
        else{
            return null
        }
    }

    private fun prepareImagePart(filepath: String): MultipartBody.Part {
        val file=File(filepath)
        val requestFile= RequestBody.create("image/jpeg".toMediaTypeOrNull(),file)
        return MultipartBody.Part.createFormData("file",file.name,requestFile)
    }
}