package io.tricefal.core.note

import java.util.*

interface INotesService<T, U> {
    fun save(note: T)
    fun update(note: T)
    fun delete(id: U)
    fun findAll(): List<T>
    fun findById(id: U): Optional<T>
    fun findByUser(user: String): List<T>
    fun findByTitle(title: String): List<T>
}

class NoteService(private var noteRepository: NotesRepository<NoteDomain, Long>) : INotesService<NoteDomain, Long> {
    override fun save(note: NoteDomain) {
        noteRepository.save(note)
    }

    override fun update(note: NoteDomain) {
        noteRepository.update(note)
    }

    override fun delete(id: Long) {
        noteRepository.delete(id)
    }

    override fun findAll(): List<NoteDomain> {
        return noteRepository.findAll()
    }

    override fun findById(id: Long): Optional<NoteDomain> {
        return noteRepository.findById(id)
    }

    override fun findByUser(author: String): List<NoteDomain> {
        return noteRepository.findByAuthor(author)
    }

    override fun findByTitle(title: String): List<NoteDomain> {
        return noteRepository.findByTitle(title)
    }
}