package io.tricefal.core.note

import org.springframework.stereotype.Service
import java.util.*

@Service
class NoteServiceAdapter(private var service: INotesService<NoteDomain, Long>) : INotesService<NoteModel, Long> {
    override fun save(note: NoteModel) {
        service.save(fromModel(note))
    }

    override fun update(note: NoteModel) {
        service.update(fromModel(note))
    }

    override fun delete(id: Long) {
        service.delete(id)
    }

    override fun findAll(): List<NoteModel> {
        return service.findAll().map { toModel(it) };
    }

    override fun findById(id: Long): Optional<NoteModel> {
        return service.findById(id).map { toModel(it) }
    }

    override fun findByUser(user: String): List<NoteModel> {
        return service.findByUser(user).map { toModel(it) }
    }

    override fun findByTitle(title: String): List<NoteModel> {
        return service.findByTitle(title).map { toModel(it) }
    }
}
