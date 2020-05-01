package io.tricefal.core.note

import org.springframework.stereotype.Repository
import java.util.*

@Repository
class NotesRepositoryAdapter(private var notesJpaRepository: NotesJpaRepository) : NotesRepository<NoteDomain, Long> {
    override fun save(note: NoteDomain) {
        println(note)
        notesJpaRepository.save(toEntity(note))
    }

    override fun delete(id: Long) {
        println(id)
        val entity = notesJpaRepository.findById(id)
        if (entity.isPresent)
            notesJpaRepository.delete(entity.get())
    }

    override fun update(note: NoteDomain) {
        println(note)
        notesJpaRepository.save(toEntity(note))
    }

    override fun findAll(): List<NoteDomain> {
        return notesJpaRepository.findAll().map {
            println("note : $it")
            fromEntity(it)
        }
    }

    override fun findById(id: Long): Optional<NoteDomain> {
        return notesJpaRepository.findById(id).map {
            println("note : $it")
            fromEntity(it)
        }
    }

    override fun findByAuthor(author: String): List<NoteDomain> {
        println("findByAuthor$author")
        return notesJpaRepository.findByAuthor(author).map {
            println("map : $author")
            fromEntity(it)
        }
    }

    override fun findByTitle(title: String): List<NoteDomain> {
        println("findByTitle$title")
        return notesJpaRepository.findByTitle(title).map {
            println("map : $title")
            fromEntity(it)
        }
    }

}





