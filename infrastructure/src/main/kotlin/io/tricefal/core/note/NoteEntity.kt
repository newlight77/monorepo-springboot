package io.tricefal.core.note

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class NoteEntity(@Id @GeneratedValue var id: Long? = null,
                var title: String? = null,
                var text: String? = null,
                @JsonIgnore var user: String? = null)

fun toEntity(domain: NoteDomain): NoteEntity {
    return NoteEntity().copy(
            domain.id,
            domain.title,
            domain.text,
            domain.author
    )
}

fun fromEntity(entity: NoteEntity): NoteDomain {
    return NoteDomain().copy(
            entity.id,
            entity.title,
            entity.text,
            entity.user
    )
}