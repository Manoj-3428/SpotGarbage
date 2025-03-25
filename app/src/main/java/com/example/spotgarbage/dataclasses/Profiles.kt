package com.example.spotgarbage.dataclasses

import com.google.firebase.firestore.PropertyName

data class Profiles(
    @PropertyName("role") var role: String = "admin",
    @PropertyName("name") var name: String = "Unknown user",
    @PropertyName("email") var email: String = "",
    @PropertyName("phone") var phone: String = "",
    @PropertyName("uri") var uri: String = "",
    @PropertyName("address") var address: String = "",
) {
    constructor() : this("", "", "", "", "", "")
}

