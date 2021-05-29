package com.example.timetracking.repository.login

import com.google.gson.annotations.SerializedName

data class LoginRequest(
        @SerializedName("username")
        var login: String? = null,
        @SerializedName("password")
        var password: String? = null
)