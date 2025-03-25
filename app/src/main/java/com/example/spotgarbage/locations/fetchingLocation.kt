package com.example.spotgarbage.locations

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

data class LocationData(val latitude: Double, val longitude: Double)

fun fetchLocationsRealtime(onResult: (List<LocationData>) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("complaints")
        .whereEqualTo("status", "Pending")
        .orderBy("timestamp", Query.Direction.DESCENDING)
        .addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Firestore", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                val locations = snapshot.documents.mapNotNull { doc ->
                    val lat = doc.getString("latitude")?.toDoubleOrNull()
                    val lng = doc.getString("longitude")?.toDoubleOrNull()
                    if (lat != null && lng != null) LocationData(lat, lng) else null
                }
                onResult(locations)
            } else {
                Log.d("Firestore", "No pending complaints found.")
                onResult(emptyList())
            }
        }
}
