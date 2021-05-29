package com.example.timetracking.repository.login

import retrofit2.Call
import retrofit2.http.*

interface LoginApi {
    @POST("authenticate/")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}