package org.skyfaced.todi.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.skyfaced.todi.database.entity.TaskEntity

@Dao
abstract class TaskDao : BaseDao<TaskEntity>() {
    @Query("SELECT * FROM task")
    abstract fun fetchTasksFlow(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE id = :id")
    abstract suspend fun fetchTaskById(id: Long): TaskEntity?
}