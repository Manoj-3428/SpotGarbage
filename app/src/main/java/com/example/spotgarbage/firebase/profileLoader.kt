package com.example.spotgarbage.firebase

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.spotgarbage.dataclasses.Profiles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.example.spotgarbage.R
import com.google.firebase.storage.StorageReference
//we should change the security rules
fun profiles(
    role: String="",
    name: String="",
    email: String="",
    phone: String="",
    uri: Uri?=null,
    address: String="",
    db: FirebaseFirestore,
    storage: FirebaseStorage,
    auth: FirebaseAuth,
    context: Context,
    onComplete:()->Unit
) {
    val user = auth.currentUser
    if (user == null) {
        Toast.makeText(context, "User is not authenticated", Toast.LENGTH_SHORT).show()
        return
    }
    val userId = user.uid
    if (uri != null) {
        val storageRef = storage.reference.child("profile_images/$userId.jpg")
        storageRef.putFile(uri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { url ->
                store(role,name, email, phone, url.toString(), address, db, auth, context,onComplete)
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Failed to get download URL: ${e.message}", Toast.LENGTH_SHORT).show()
                onComplete()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(context, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            onComplete()
        }
    } else {
        store(role,name, email, phone, "", address, db, auth, context,onComplete)
    }
}

fun store(
    role: String,
    name: String,
    email: String,
    phone: String,
    uri: String,
    address: String,
    db: FirebaseFirestore,
    auth: FirebaseAuth,
    context: Context,
    onComplete: () -> Unit
) {
    val user = auth.currentUser
    if (user == null) {
        Toast.makeText(context, "User is not authenticated", Toast.LENGTH_SHORT).show()
        return
    }

    val uid = user.uid
    val profile = Profiles(role,name, email, phone, uri.toString(), address.toString())

    db.collection("users").document(uid).set(profile)
        .addOnSuccessListener {
            Toast.makeText(context, "Your Profile updated Successfully", Toast.LENGTH_SHORT).show()
            onComplete()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            onComplete()
        }
}

