package org.skyfaced.noti.ui.model.note

import org.skyfaced.noti.database.entity.NoteEntity
import org.skyfaced.noti.ui.model.Mapper

class NoteMapper : Mapper<NoteEntity, Note> {
    override fun map(input: NoteEntity) = input.run { Note(id, title, description) }
}