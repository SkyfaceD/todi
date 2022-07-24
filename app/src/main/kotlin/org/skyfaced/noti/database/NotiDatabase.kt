package org.skyfaced.noti.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.skyfaced.noti.database.dao.NoteDao
import org.skyfaced.noti.database.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class NotiDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
}