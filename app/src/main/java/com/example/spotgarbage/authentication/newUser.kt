package com.example.spotgarbage.authentication

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.spotgarbage.dataclasses.Profiles
import com.example.spotgarbage.firebase.profiles
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
fun storing(context: Context,name: String, email: String, role:String,db: FirebaseFirestore,auth: FirebaseAuth){
    var user=auth.currentUser
    if(user==null){
        Toast.makeText(context, "User is not authenticated", Toast.LENGTH_SHORT).show()
    }
    else{
        val uid=user.uid
        val profile=Profiles(role=role,name=name,email=email,phone="",uri="",address="")
        db.collection("users").document(uid).set(profile)
            .addOnSuccessListener {
                Toast.makeText(context, "Your Profile updated Successfully", Toast.LENGTH_SHORT).show()
            }
    }
}