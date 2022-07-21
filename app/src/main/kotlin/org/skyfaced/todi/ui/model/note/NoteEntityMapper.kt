package org.skyfaced.todi.ui.model.note

import org.skyfaced.todi.database.entity.NoteEntity
import org.skyfaced.todi.ui.model.Mapper

class NoteEntityMapper : Mapper<Note, NoteEntity> {
    override fun map(input: Note) = input.run { NoteEntity(id, title, description) }
}