package com.example.spotgarbage.authentication

import com.google.firebase.auth.FirebaseAuth

fun createUser(email: String, password: String, onComplete: (String) -> Unit) {
    val auth = FirebaseAuth.getInstance()

    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            onComplete("success")
        } else {
            onComplete("Failed to create user: ${task.exception?.message}")
        }
    }
}