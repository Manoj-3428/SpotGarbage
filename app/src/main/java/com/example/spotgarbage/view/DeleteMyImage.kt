package com.example.spotgarbage.view

import com.google.firebase.storage.FirebaseStorage

fun DeleteMyImage(imageUri: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUri)
    storageRef.delete().addOnSuccessListener {
        onSuccess()
    }.addOnFailureListener {
        onFailure(it.message.toString())
    }
}