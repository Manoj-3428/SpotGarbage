package com.example.spotgarbage.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.spotgarbage.dataclasses.Complaint
import com.google.firebase.firestore.FirebaseFirestore

class ComplaintViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _posts = mutableStateListOf<Complaint>()
    val complaintList: List<Complaint> = _posts

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        firestore.collection("complaints")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val postList = snapshot.toObjects(Complaint::class.java)
                    _posts.clear()
                    _posts.addAll(postList)
                } else {
                    Log.d("Firestore", "No complaints found.")
                }
            }
    }
}