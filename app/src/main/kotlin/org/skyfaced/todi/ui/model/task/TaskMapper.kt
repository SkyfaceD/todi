package org.skyfaced.todi.ui.model.task

import org.skyfaced.todi.database.entity.TaskEntity
import org.skyfaced.todi.ui.model.Mapper

class TaskEntityMapper : Mapper<TaskEntity, Task> {
    override fun map(input: TaskEntity) = input.run { Task(id, title, description) }
}