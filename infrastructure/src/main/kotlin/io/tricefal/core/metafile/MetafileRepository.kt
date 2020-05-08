package io.tricefal.core.metafile

import io.tricefal.core.util.FileStorage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.io.InputStream
import java.nio.file.Paths
import java.util.*

@Repository
class MetafileRepository(val repository: MetafileJpaRepository) {

    @Value("{data.files.path}")
    lateinit var dataFilesPath: String

    fun save(metafile: MetafileDomain, inpputStream: InputStream) {
        repository.save(toEntity(metafile))
        FileStorage().save(inpputStream, Paths.get(dataFilesPath))
    }

    fun findById(id: Long): Optional<MetafileDomain> {
        println("findById $id")
        return repository.findById(id).map {
            println("map : $id")
            fromEntity(it)
        }
    }

    fun findByFilename(filename: String): List<MetafileDomain> {
        println("findByUsername$filename")
        return repository.findByFilename(filename).map {
            println("map : $filename")
            fromEntity(it)
        }
    }

    fun findByUsername(username: String): List<MetafileDomain> {
        println("findByUsername$username")
        return repository.findByUsername(username).map {
            println("map : $username")
            fromEntity(it)
        }
    }
}
