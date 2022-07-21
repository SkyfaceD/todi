package org.skyfaced.todi.ui.model.note

import org.skyfaced.todi.database.entity.NoteEntity
import org.skyfaced.todi.ui.model.Mapper

class NoteEntityMapper : Mapper<NoteEntity, Note> {
    override fun map(input: NoteEntity) = input.run { Note(id, title, description) }
}