package io.tricefal.core.note

import org.springframework.stereotype.Repository
import java.util.*

@Repository
class NotesRepositoryAdapter(private var notesJpaRepository: NotesJpaRepository) : NotesRepository<NoteDomain, Long> {
    override fun save(note: NoteDomain) {
        println(note)
        notesJpaRepository.save(fromDomain(note))
    }

    override fun delete(id: Long) {
        println(id)
        val entity = notesJpaRepository.findById(id)
        if (entity.isPresent)
            notesJpaRepository.delete(entity.get())
    }

    override fun update(note: NoteDomain) {
        println(note)
        notesJpaRepository.save(fromDomain(note))
    }

    override fun findAll(): List<NoteDomain> {
        return notesJpaRepository.findAll().map {
            println("note : $it")
            toDomain(it)
        }
    }

    override fun findById(id: Long): Optional<NoteDomain> {
        return notesJpaRepository.findById(id).map {
            println("note : $it")
            toDomain(it)
        }
    }

    override fun findByUser(user: String): List<NoteDomain> {
        println("findByUser$user")
        return notesJpaRepository.findByUser(user).map {
            println("map : $user")
            toDomain(it)
        }
    }

    override fun findByTitle(title: String): List<NoteDomain> {
        println("findByTitle$title")
        return notesJpaRepository.findByTitle(title).map {
            println("map : $title")
            toDomain(it)
        }
    }

    fun fromDomain(domain: NoteDomain): NoteEntity {
        return NoteEntity().copy(
                domain.id,
                domain.title,
                domain.text,
                domain.author
        )
    }

    fun toDomain(entity: NoteEntity): NoteDomain {
        return NoteDomain().copy(
                entity.id,
                entity.title,
                entity.text,
                entity.user
        )
    }
}





