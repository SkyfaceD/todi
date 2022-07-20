package org.skyfaced.todi.ui.screen.details

import org.skyfaced.todi.database.dao.TaskDao
import org.skyfaced.todi.ui.model.task.Task
import org.skyfaced.todi.ui.model.task.TaskEntityMapper

class DetailsRepositoryImpl(
    private val taskDao: TaskDao,
    private val taskEntityMapper: TaskEntityMapper,
): DetailsRepository {
    override suspend fun fetchTaskById(id: Long): Task {
        val entity = taskDao.fetchTaskById(id) ?: throw Exception("") // TODO Handle exceptions
        return taskEntityMapper.map(entity)
    }
}