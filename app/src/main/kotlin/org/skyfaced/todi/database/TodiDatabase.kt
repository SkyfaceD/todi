package org.skyfaced.todi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.skyfaced.todi.database.dao.NoteDao
import org.skyfaced.todi.database.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class TodiDatabase: RoomDatabase() {
    abstract val noteDao: NoteDao
}