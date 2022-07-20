package org.skyfaced.todi.ui.screen.details

import org.skyfaced.todi.ui.model.task.Task

interface DetailsRepository {
    suspend fun fetchTaskById(id: Long): Task
}