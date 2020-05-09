package io.tricefal.core.note

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("notes")
class NoteApi(val service: INotesService<NoteModel, Long>) {

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody note: NoteModel) {
        service.save(note)
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable id: Long, @RequestBody note: NoteModel) {
        service.update(note)
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    fun byId(@PathVariable id: Long): NoteModel {
        val note = service.findById(id)
        return if (note.isPresent) {
            note.get()
        } else
        {
            NoteModel()
        }
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun byTitle(@RequestParam title: String) : List<NoteModel>{
        return if (title.isEmpty()) {
            service.findAll()
        } else {
            service.findByTitle(title)
        }
    }

}