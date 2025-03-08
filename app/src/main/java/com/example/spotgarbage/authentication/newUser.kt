package com.example.spotgarbage.authentication

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.example.spotgarbage.firebase.profiles
import com.example.spotgarbage.firebase.storing
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun createUser(context:Context,role:String,name: String, email: String, password: String, onComplete: (String) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val db=FirebaseFirestore.getInstance()
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            storing(context,name,email,role,db,auth)
            onComplete("success")

        } else {
            onComplete("Failed to create user: ${task.exception?.message}")
        }
    }
}
//context: Context,name: String, email: String, role:String,db: FirebaseFirestore,auth: FirebaseAuth