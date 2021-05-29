package com.example.timetracking.repository.login

import com.example.timetracking.repository.sessionmanager.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class DefaultLoginRepository @Inject constructor(
    private val sessionManager: SessionManager,
    private val loginApi: LoginApi
) : LoginRepository {

    companion object {
        const val loginApiErrorMessage = "Ошибка, проверьте правильность введенных значений."
    }

    override suspend fun login(
        loginRequest: LoginRequest,
        loading: () -> Unit,
        success: () -> Unit,
        error: (Throwable) -> Unit
    ) {
        loading.invoke()
        loginApi
            .login(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val loginResponse = response.body()

                    if (loginResponse != null) {
                        sessionManager.saveTokens(
                            loginResponse.accessToken,
                            loginResponse.refreshToken
                        )
                        success.invoke()
                    } else {
                        Timber.w(response.toString())
                        error.invoke(NullPointerException(loginApiErrorMessage))
                    }
                }

                override fun onFailure(
                    call: Call<LoginResponse>,
                    t: Throwable
                ) {
                    error.invoke(t)
                }
            })
    }

    override suspend fun saveUser(
        login: String,
        password: String
    ) {
        sessionManager.saveUser(login, password)
    }

    override suspend fun getSavedUser(func: (LoginRequest) -> Unit) {
        func.invoke(sessionManager.fetchUser())
    }
}
