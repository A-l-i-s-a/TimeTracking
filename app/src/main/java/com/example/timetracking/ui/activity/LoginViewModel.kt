package com.example.timetracking.ui.activity

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timetracking.repository.login.LoginRepository
import com.example.timetracking.repository.login.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    companion object {
        const val loginViewModelErrorMessage = "Не все поля заполнены."
    }

    fun login(
        loginRequest: LoginRequest,
        loading: () -> Unit,
        success: () -> Unit,
        error: (Throwable) -> Unit
    ) = viewModelScope.launch {
        if (loginRequest.login.isNullOrEmpty() || loginRequest.password.isNullOrEmpty()) {
            error.invoke(NullPointerException(loginViewModelErrorMessage))
        } else {
            repository.login(
                loginRequest = loginRequest,
                loading = loading,
                success = success,
                error = error
            )
        }
    }

    fun saveUser(
        login: String,
        password: String
    ) = viewModelScope.launch {
        repository.saveUser(
            login = login,
            password = password
        )
    }

    fun getSaveUser(
        func: (LoginRequest) -> Unit
    ) = viewModelScope.launch {
        repository.getSavedUser(func)
    }
}