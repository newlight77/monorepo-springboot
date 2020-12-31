package io.tricefal.core.note

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Instant

data class NoteModel(var id: Long? = null,
                     var title: String? = null,
                     var text: String? = null,
                     @JsonIgnore var author: String? = null,
                    var lastDate: Instant? = null)

fun toModel(domain: NoteDomain): NoteModel {
    return NoteModel().copy(
            domain.id,
            domain.title,
            domain.text,
            domain.author,
            domain.lastDate
    )
}

fun fromModel(model: NoteModel): NoteDomain {
    return NoteDomain().copy(
            model.id,
            model.title,
            model.text,
            model.author,
            model.lastDate
    )
}
