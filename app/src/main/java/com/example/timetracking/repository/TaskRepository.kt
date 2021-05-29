package com.example.timetracking.repository

import com.example.timetracking.database.Task
import com.example.timetracking.repository.local_data_source.room.CacheMapper
import com.example.timetracking.repository.local_data_source.room.TaskCacheEntity
import com.example.timetracking.repository.local_data_source.room.TaskDatabaseDao
import com.example.timetracking.repository.remote_data_source.retrofit.NetworkMapper
import com.example.timetracking.repository.remote_data_source.retrofit.TaskRemoteDataSource
import com.example.timetracking.util.DataState
import com.example.timetracking.util.showToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TaskRepository @Inject constructor(
    private val taskLocalDataSource: TaskDatabaseDao,
    private val taskRemoteDataSource: TaskRemoteDataSource,
    private val cacheMapper: CacheMapper,
    private val networkMapper: NetworkMapper
) {
    suspend fun getTasks(): Flow<DataState<List<Task>>> = flow {
        emit(DataState.Loading)
        try {
//            val networkData = taskRemoteDataSource.getData()
//            val tasks = networkMapper.mapFromDataEntity(networkData)
//            for (task in tasks) {
//                taskLocalDataSource.insert(cacheMapper.mapToEntity(task))
//            }
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Timber.e(e.fillInStackTrace())
        } finally {
            val cacheTasks = taskLocalDataSource.getAllTasks()
            emit(DataState.Success(cacheMapper.mapFromEntityList(cacheTasks)))
            Timber.i(cacheTasks.joinToString())
        }
    }

    suspend fun getTaskById(id: Long): Flow<DataState<Task?>> = flow {
        emit(DataState.Loading)
        try {
            val cacheTask: TaskCacheEntity? = taskLocalDataSource.get(id)
            emit(DataState.Success(cacheTask?.let { cacheMapper.mapFromEntity(it) }))
            Timber.i(cacheTask.toString())
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Timber.e(e.fillInStackTrace())
        }
    }

    suspend fun getTasksByDate(date: Long): Flow<DataState<List<Task>>> = flow {
        emit(DataState.Loading)
        try {
            val cacheTasks = taskLocalDataSource.getTaskByDate(date)
            emit(DataState.Success(cacheMapper.mapFromEntityList(cacheTasks)))
            Timber.i(cacheTasks.joinToString())
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Timber.e(e.fillInStackTrace())
        }
    }

    suspend fun createTask(task: Task): Flow<DataState<Task>> = flow {
        try {
            emit(DataState.Loading)
            try {
                taskRemoteDataSource.saveTask(networkMapper.mapToEntity(task))
            } catch (ex: java.lang.Exception) {
                showToast("Проблема с подключением к интернету")
                task.isNeedSynchronization = true
                Timber.e(ex.fillInStackTrace())
            }
            taskLocalDataSource.insert(cacheMapper.mapToEntity(task))
            emit(DataState.Success(task))
            Timber.i(task.toString())

        } catch (e: Exception) {
            emit(DataState.Error(e))
            Timber.e(e.fillInStackTrace())
        }
    }

    suspend fun synchronization(): Flow<DataState<List<Task>>> = flow {
        emit(DataState.Loading)
        try {
            val cacheTasks = taskLocalDataSource.getTasksByIsNeedSynchronization()
            for (task in cacheTasks) {
                try {
                    taskRemoteDataSource.saveTask(
                        networkMapper.mapToEntity(
                            cacheMapper.mapFromEntity(
                                task
                            )
                        )
                    )
                    task.isNeedSynchronization = false
                    taskLocalDataSource.insert(task)
                } catch (ex: java.lang.Exception) {
                    Timber.e(ex.fillInStackTrace())
                }
            }
            emit(DataState.Success(cacheMapper.mapFromEntityList(cacheTasks)))
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Timber.e(e.fillInStackTrace())
        }
    }

    suspend fun getTodoTasks(): Flow<DataState<List<Task>>> = flow {
        emit(DataState.Loading)
        try {
            val cacheTasks = taskLocalDataSource.getTodoTasks()
            emit(DataState.Success(cacheMapper.mapFromEntityList(cacheTasks)))
            Timber.i(cacheTasks.joinToString())
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Timber.e(e.fillInStackTrace())
        }
    }

    suspend fun getDoneTasks(): Flow<DataState<List<Task>>> = flow {
        emit(DataState.Loading)
        try {
            val cacheTasks = taskLocalDataSource.getDoneTasks()
            emit(DataState.Success(cacheMapper.mapFromEntityList(cacheTasks)))
            Timber.i(cacheTasks.joinToString())
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Timber.e(e.fillInStackTrace())
        }
    }

    suspend fun getTaskByDate(date: Long): Flow<DataState<List<Task>>> = flow {
        emit(DataState.Loading)
        try {
            val cacheTasks = taskLocalDataSource.getTaskByDate(date)
            emit(DataState.Success(cacheMapper.mapFromEntityList(cacheTasks)))
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Timber.e(e.fillInStackTrace())
        }
    }
}