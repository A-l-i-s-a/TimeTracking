package com.example.timetracking.repository.sessionmanager

import android.content.Context
import android.content.SharedPreferences
import com.example.timetracking.R
import com.example.timetracking.repository.login.LoginRequest
import javax.inject.Inject

class SessionManager @Inject constructor(
    context: Context
) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN_ACCESS = "user_token_access"
        const val USER_TOKEN_REFRESH = "user_token_refresh"
        const val USER_LOGIN = "user_login"
        const val USER_PASSWORD = "user_password"
    }

    fun saveTokens(tokenAccess: String? = null, tokenRefresh: String? = null) {
        val editor = prefs.edit()
        if (tokenAccess != null) editor.putString(USER_TOKEN_ACCESS, tokenAccess)
        if (tokenRefresh != null) editor.putString(USER_TOKEN_REFRESH, tokenRefresh)
        editor.apply()
    }

    fun saveUser(login: String, password: String) {
        val editor = prefs.edit()
        editor.putString(USER_LOGIN, login)
        editor.putString(USER_PASSWORD, password)
        editor.apply()
    }

    fun fetchAccessToken() = prefs.getString(USER_TOKEN_ACCESS, null)

    fun fetchRefreshToken() = prefs.getString(USER_TOKEN_REFRESH, null)

    fun fetchUser() = LoginRequest(
        prefs.getString(USER_LOGIN, null),
        prefs.getString(USER_PASSWORD, null)
    )
}