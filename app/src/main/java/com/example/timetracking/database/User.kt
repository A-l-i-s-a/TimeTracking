package com.example.timetracking.database

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id")
    var id: Long,

    @SerializedName("first_name")
    var firstName: String,

    @SerializedName("last_name")
    var lastName: String,

    @SerializedName("email")
    var email: String
)