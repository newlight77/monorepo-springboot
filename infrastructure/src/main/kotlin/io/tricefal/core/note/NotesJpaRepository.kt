package io.tricefal.core.note

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository
import java.util.*

interface NotesJpaRepository : JpaRepository<NoteEntity, Long> {
    fun save(entity: NoteEntity)
    override fun delete(entity: NoteEntity)
    override fun findAll(): List<NoteEntity>
    override fun findById(id: Long): Optional<NoteEntity>
    @Query("SELECT t FROM NoteEntity t where t.author like %:author%")
    fun findByAuthor(author: String): List<NoteEntity>
    @Query("SELECT t FROM NoteEntity t where t.title like %:title%")
    fun findByTitle(title: String): List<NoteEntity>
}
