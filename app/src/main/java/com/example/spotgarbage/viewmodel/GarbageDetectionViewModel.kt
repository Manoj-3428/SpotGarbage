package com.example.spotgarbage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotgarbage.api.GarbageDetectionRepository
import kotlinx.coroutines.launch

class GarbageDetectionViewModel : ViewModel() {
    private val repository = GarbageDetectionRepository()

    fun detectGarbage(filePath: String, apiKey: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val result = repository.detectGarbage(filePath, apiKey)
                val message = if (result?.predictions.isNullOrEmpty()) {
                    "No garbage detected"
                } else {
                    result?.predictions?.joinToString("\n") {
                        "${it.`class`} detected with ${it.confidence * 100}% confidence"
                    } ?: "Detection failed"
                }
                onResult(message)
            } catch (e: Exception) {
                onResult("Error: ${e.localizedMessage}")
            }
        }
    }
}