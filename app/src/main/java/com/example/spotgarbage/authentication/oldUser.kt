package com.example.spotgarbage.authentication

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.spotgarbage.dataclasses.Profiles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun loginUser(role: String, email: String, password: String, onComplete: (String) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                checkingUserData(role, db, userId) { check ->
                    if (check) {
                        onComplete("success")
                    } else {
                        onComplete("User not found or role mismatch")
                    }
                }
            } else {
                onComplete("User ID not found")
            }
        } else {
            onComplete("Failed to login: Check the Email and password once again")
        }
    }
}

fun checkingUserData(role: String, db: FirebaseFirestore, userId: String, onComplete: (Boolean) -> Unit) {
    db.collection("users").document(userId).get().addOnSuccessListener { document ->
        val user = document.toObject(Profiles::class.java)
        if (user != null && user.role == role) {
            onComplete(true)
        } else {
            onComplete(false)
        }
    }.addOnFailureListener {
        onComplete(false)
    }
}
