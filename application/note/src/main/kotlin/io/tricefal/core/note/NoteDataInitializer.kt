package io.tricefal.core.note

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class NoteDataInitializer(val service: INotesService<NoteModel, Long>) : ApplicationRunner {
    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        modelData().map {fromModel(it)}
                .forEach() {
                    service.save(toModel(it))
                }
        service.findAll().forEach { println(it) }
    }
}

fun modelData(): List<NoteModel> {
    return listOf(
            NoteModel(title = "Note 1", text = "Some text to enable scrolling.. Lorem ipsum dolor sit amet", author = "user")
    )
}