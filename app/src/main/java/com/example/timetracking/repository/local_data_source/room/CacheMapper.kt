package com.example.timetracking.repository.local_data_source.room

import android.net.Uri
import com.example.timetracking.database.Task
import com.example.timetracking.util.EntityMapper
import java.sql.Timestamp
import javax.inject.Inject

class CacheMapper
@Inject
constructor() :
    EntityMapper<TaskCacheEntity, Task> {

    override fun mapFromEntity(entity: TaskCacheEntity): Task {
        val attachments = entity.attachments.map { Uri.parse(it) }
        return Task(
            id = entity.id,
            headline = entity.headline,
            description = entity.description,
            timeBeginning = entity.timeBeginning?.let { Timestamp(it) },
            timeEnd = entity.timeEnd?.let { Timestamp(it) },
            attachments = attachments,
            isNeedSynchronization = entity.isNeedSynchronization
        )
    }

    override fun mapToEntity(domainModel: Task): TaskCacheEntity {
        val map = domainModel.attachments.map { it.toString() }.toTypedArray()
        return TaskCacheEntity(
            id = domainModel.id,
            headline = domainModel.headline,
            description = domainModel.description,
            timeBeginning = domainModel.timeBeginning?.time,
            timeEnd = domainModel.timeEnd?.time,
            attachments = map.asList(),
            isNeedSynchronization = domainModel.isNeedSynchronization

        )
    }

    fun mapFromEntityList(entities: List<TaskCacheEntity>): List<Task> {
        return entities.map { mapFromEntity(it) }
    }
}


