package com.example.timetracking.di

import android.content.Context
import androidx.room.Room
import com.example.timetracking.repository.local_data_source.room.TaskDatabase
import com.example.timetracking.repository.local_data_source.room.TaskDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext appContext: Context): TaskDatabase {
        return Room.databaseBuilder(
            appContext,
            TaskDatabase::class.java,
            "task_history_database"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTaskDatabaseDao(appDatabase: TaskDatabase): TaskDatabaseDao {
        return appDatabase.taskDatabaseDao
    }
}