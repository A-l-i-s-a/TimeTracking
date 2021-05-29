package com.example.timetracking.di

import com.example.timetracking.repository.TaskRepository
import com.example.timetracking.repository.local_data_source.room.CacheMapper
import com.example.timetracking.repository.local_data_source.room.TaskDatabaseDao
import com.example.timetracking.repository.remote_data_source.retrofit.NetworkMapper
import com.example.timetracking.repository.remote_data_source.retrofit.TaskRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideMainRepository(
        dataSource: TaskDatabaseDao,
        remoteDataSource: TaskRemoteDataSource,
        cacheMapper: CacheMapper,
        networkMapper: NetworkMapper
    ): TaskRepository {
        return TaskRepository(dataSource, remoteDataSource, cacheMapper, networkMapper)
    }
}