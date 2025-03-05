package com.example.spotgarbage.api

data class DetectionResponse(
    val predictions: List<Prediction>
)
data class Prediction(
    val `class`:String,
    val confidence: Float
)