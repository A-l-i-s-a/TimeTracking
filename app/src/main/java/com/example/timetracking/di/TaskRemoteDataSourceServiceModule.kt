package com.example.timetracking.di

import com.example.timetracking.repository.remote_data_source.retrofit.TaskRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class TaskRemoteDataSourceServiceModule {

    @Singleton
    @Provides
    fun provideTaskRemoteDataSourceService(
        retrofit: Retrofit.Builder,
        @Named("MainClient")
        client: OkHttpClient
    ): TaskRemoteDataSource {
        return retrofit
            .client(client)
            .build()
            .create(TaskRemoteDataSource::class.java)
    }
}