package io.tricefal.core.note

import com.fasterxml.jackson.annotation.JsonIgnore

data class NoteModel(var id: Long? = null,
                     var title: String? = null,
                     var text: String? = null,
                     @JsonIgnore var author: String? = null)

fun toModel(domain: NoteDomain): NoteModel {
    return NoteModel().copy(
            domain.id,
            domain.title,
            domain.text,
            domain.author
    )
}

fun fromModel(model: NoteModel): NoteDomain {
    return NoteDomain().copy(
            model.id,
            model.title,
            model.text,
            model.author
    )
}
