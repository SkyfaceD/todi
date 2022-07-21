package org.skyfaced.todi.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

abstract class BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entities: List<T>): LongArray

    @Update
    abstract suspend fun update(entity: T): Int

    @Update
    abstract suspend fun update(entities: List<T>): Int

    @Delete
    abstract suspend fun delete(entity: T): Int

    @Delete
    abstract suspend fun delete(entities: List<T>): Int
}