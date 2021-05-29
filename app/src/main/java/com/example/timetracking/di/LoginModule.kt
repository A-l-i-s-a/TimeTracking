package com.example.timetracking.di

import com.example.timetracking.repository.login.DefaultLoginRepository
import com.example.timetracking.repository.login.LoginApi
import com.example.timetracking.repository.login.LoginRepository
import com.example.timetracking.repository.sessionmanager.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object LoginModule {

    @Singleton
    @Provides
    fun provideLoginApi(
        retrofit: Retrofit.Builder
    ): LoginApi {
        return retrofit
            .build()
            .create(LoginApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(
        sessionManager: SessionManager,
        loginApi: LoginApi
    ) = DefaultLoginRepository(
        sessionManager = sessionManager,
        loginApi = loginApi
    ) as LoginRepository
}