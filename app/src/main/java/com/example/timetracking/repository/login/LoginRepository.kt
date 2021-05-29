package com.example.timetracking.repository.login

interface LoginRepository {
    suspend fun login(
        loginRequest: LoginRequest,
        loading: () -> Unit,
        success: () -> Unit,
        error: (Throwable) -> Unit
    )

    suspend fun saveUser(
        login: String,
        password: String
    )

    suspend fun getSavedUser(
        func: (LoginRequest) -> Unit
    )
}
