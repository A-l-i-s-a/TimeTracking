package com.example.timetracking.repository.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("jwtToken")
    var accessToken: String,

    @SerializedName("refresh")
    var refreshToken: String
)