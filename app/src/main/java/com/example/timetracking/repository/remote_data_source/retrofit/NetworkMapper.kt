package com.example.timetracking.repository.remote_data_source.retrofit

import com.example.timetracking.database.Task
import com.example.timetracking.util.EntityMapper
import com.example.timetracking.util.saveFile
import javax.inject.Inject

class NetworkMapper
@Inject
constructor() :
    EntityMapper<TaskNetworkEntity, Task> {

    override fun mapFromEntity(entity: TaskNetworkEntity): Task {
        return Task(
            id = entity.id,
            headline = entity.name,
            description = entity.description,
            timeBeginning = entity.dateStart,
            timeEnd = entity.dateFinish
        )
    }

    override fun mapToEntity(domainModel: Task): TaskNetworkEntity {
        val attachmentsUrls = domainModel.attachments.map { saveFile(it) }
        return TaskNetworkEntity(
            id = domainModel.id,
            name = domainModel.headline,
            description = domainModel.description,
            dateStart = domainModel.timeBeginning,
            dateFinish = domainModel.timeEnd,
            attachmentsUrls = attachmentsUrls as MutableList<String>
        )
    }

    fun mapFromEntityList(entities: List<TaskNetworkEntity>): List<Task> {
        return entities.map { mapFromEntity(it) }
    }

    fun mapFromDataEntity(data: DataNetworkEntity): List<Task> {
        return data.task.values.map { mapFromEntity(it) }
    }

}