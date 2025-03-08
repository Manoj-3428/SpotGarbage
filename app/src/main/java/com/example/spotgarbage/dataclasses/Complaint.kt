package com.example.spotgarbage.dataclasses
import kotlinx.parcelize.Parcelize

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
@Parcelize
data class Complaint(
    val userId: String="",
    val timestamp: Timestamp=Timestamp.now(),
    val username: String="",
    val postId: String="",
    val address: String="",
    val imageUri: String="",
    val type: String="",
    val description: String="",
    val location: String="",
    val latitude: String="",
    val longitude: String="",
    val formattedDate: String="",
    val dayOfWeek: String="",
    val formattedTime: String="",
    val email:String="",
    val status:String="Pending",
    val clearedOn:String=" "
): Parcelable{
    constructor() : this("", Timestamp.now(),"", "", "", "", "","","","","","","","","","")


}

