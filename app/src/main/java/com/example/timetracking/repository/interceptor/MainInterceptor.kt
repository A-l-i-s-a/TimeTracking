package com.example.timetracking.repository.interceptor

import com.example.timetracking.repository.sessionmanager.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class MainInterceptor @Inject constructor(
    private val sessionManager : SessionManager
) : Interceptor {

    companion object {
        const val HEADER_NAME = "Authorization"
        const val BASE_VALUE = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        sessionManager.fetchAccessToken()?.let {
            requestBuilder.addHeader(HEADER_NAME, "$BASE_VALUE $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}