package io.tricefal.core.note

import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class NotesRepositoryAdapter(private var notesJpaRepository: NotesJpaRepository) : NotesRepository<NoteDomain, Long> {
    override fun save(note: NoteDomain) {
        val entity = toEntity(note)
        entity.lastDate = note.lastDate ?: Instant.now()
        notesJpaRepository.save(entity)
    }

    override fun delete(id: Long) {
        val entity = notesJpaRepository.findById(id)
        if (entity.isPresent)
            notesJpaRepository.delete(entity.get())
    }

    override fun update(note: NoteDomain) {
        val entity = toEntity(note)
        entity.lastDate = Instant.now()
        notesJpaRepository.save(entity)
    }

    override fun findAll(): List<NoteDomain> {
        return notesJpaRepository.findAll().map {
            fromEntity(it)
        }
    }

    override fun findById(id: Long): Optional<NoteDomain> {
        return notesJpaRepository.findById(id).map {
            fromEntity(it)
        }
    }

    override fun findByAuthor(author: String): List<NoteDomain> {
        return notesJpaRepository.findByAuthor(author).map {
            fromEntity(it)
        }
    }

    override fun findByTitle(title: String): List<NoteDomain> {
        return notesJpaRepository.findByTitle(title).map {
            fromEntity(it)
        }
    }

}





