package io.tricefal.core.note

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "note")
data class NoteEntity(
//                      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//                      @SequenceGenerator(name = "sequenceGenerator")
                      @GeneratedValue(strategy = GenerationType.IDENTITY)
                      @Id
                      var id: Long? = null,
                      var title: String? = null,
                      var text: String? = null,
                      @JsonIgnore var author: String? = null,
                      var lastDate: Instant? = null)

fun toEntity(domain: NoteDomain): NoteEntity {
    return NoteEntity().copy(
            domain.id,
            domain.title,
            domain.text,
            domain.author,
            domain.lastDate
    )
}

fun fromEntity(entity: NoteEntity): NoteDomain {
    return NoteDomain().copy(
            entity.id,
            entity.title,
            entity.text,
            entity.author,
            entity.lastDate
    )
}