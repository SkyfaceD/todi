package org.skyfaced.todi.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.skyfaced.todi.database.entity.NoteEntity

@Dao
abstract class NoteDao : BaseDao<NoteEntity>() {
    @Query("SELECT * FROM note")
    abstract fun fetchNotesFlow(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE id = :id")
    abstract suspend fun fetchNoteById(id: Long): NoteEntity?
}