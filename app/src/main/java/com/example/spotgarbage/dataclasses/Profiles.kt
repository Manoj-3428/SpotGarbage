package com.example.spotgarbage.dataclasses

data class Profiles(
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var uri: String = "",
    var address: String = "",
) {
    constructor() : this("", "", "", "", "")
}
