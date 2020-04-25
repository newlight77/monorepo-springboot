package io.tricefal.core.note

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NoteConfiguration {
    @Bean
    fun noteService(repository: NotesRepository<NoteDomain, Long>): INotesService<NoteDomain, Long> {
        return NoteService(repository)
    }
}