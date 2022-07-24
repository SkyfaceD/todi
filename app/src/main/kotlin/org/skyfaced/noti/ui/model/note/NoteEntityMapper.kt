package org.skyfaced.noti.ui.model.note

import org.skyfaced.noti.database.entity.NoteEntity
import org.skyfaced.noti.ui.model.Mapper

class NoteEntityMapper : Mapper<Note, NoteEntity> {
    override fun map(input: Note) = input.run { NoteEntity(id, title, description) }
}