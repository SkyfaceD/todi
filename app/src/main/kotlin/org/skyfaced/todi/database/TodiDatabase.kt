package org.skyfaced.todi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.skyfaced.todi.database.dao.TaskDao
import org.skyfaced.todi.database.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class TodiDatabase: RoomDatabase() {
    abstract val taskDao: TaskDao
}