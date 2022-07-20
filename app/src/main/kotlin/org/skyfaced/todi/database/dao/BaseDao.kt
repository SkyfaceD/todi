package org.skyfaced.todi.database.dao

import androidx.room.*

abstract class BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(entities: List<T>): LongArray

    @Update
    abstract suspend fun update(entity: T)

    @Update
    abstract suspend fun update(entities: List<T>)

    @Delete
    abstract suspend fun delete(entity: T)

    @Delete
    abstract suspend fun delete(entities: List<T>)

    @Transaction
    open suspend fun upsert(entity: T) {
        val rowId = insert(entity)
        if (rowId == -1L) update(entity)
    }

    @Transaction
    open suspend fun upsert(entities: List<T>) {
        val updateEntities = mutableListOf<T>()
        for ((idx, rowIds) in insert(entities).withIndex()) {
            if (rowIds == -1L) updateEntities.add(entities[idx])
        }
        update(updateEntities)
    }
}