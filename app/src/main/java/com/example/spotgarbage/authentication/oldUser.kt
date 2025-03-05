package com.example.spotgarbage.authentication

import com.google.firebase.auth.FirebaseAuth

fun loginUser(email:String,password:String,onComplete :(String)->Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            onComplete("success")
        } else {
            onComplete("Failed to login: ${task.exception?.message}")
        }
    }
}