package com.example.spotgarbage.authentication

import com.google.firebase.auth.FirebaseAuth

fun logout(onSuccess:()->Unit,onFailure:(String)->Unit){
    try {
        FirebaseAuth.getInstance().signOut()
        onSuccess()
    }
    catch (e: Exception){
        onFailure(e.message.toString())

    }



}