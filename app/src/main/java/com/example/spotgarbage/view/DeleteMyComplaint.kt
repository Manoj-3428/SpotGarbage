package com.example.spotgarbage.view

import com.google.firebase.firestore.FirebaseFirestore

fun DeleteMyComplaint(postId:String,onSuccess:()->Unit,onFailure:(String)->Unit){
    val db= FirebaseFirestore.getInstance()
    db.collection("complaints").document(postId).delete().addOnSuccessListener({
        onSuccess()
    }).addOnFailureListener({
        onFailure(it.message.toString())
    })
}