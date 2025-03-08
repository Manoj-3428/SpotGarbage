package com.example.spotgarbage.dataclasses

data class Profiles(
    var role:String="admin",
    var name: String = "Unknown user",
    var email: String = "",
    var phone: String = "",
    var uri: String = "",
    var address: String = "",
) {
    constructor() : this("", "", "", "", "")

}
data class UserProfiles(
    val role:String,
    var name: String = "Unknown user",
    var email: String = "",
) {
    constructor() : this("", "", "")

}
