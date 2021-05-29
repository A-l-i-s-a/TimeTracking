package com.example.timetracking.di

import com.example.timetracking.repository.interceptor.MainInterceptor
import com.example.timetracking.repository.sessionmanager.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object MainModule {

    @Singleton
    @Provides
    fun provideMainInterceptor(sessionManager: SessionManager): MainInterceptor {
        return MainInterceptor(sessionManager)
    }

    @Singleton
    @Provides
    @Named("MainClient")
    fun provideMainOkhttpClient(mainInterceptor: MainInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(mainInterceptor)
            .build()
    }
}