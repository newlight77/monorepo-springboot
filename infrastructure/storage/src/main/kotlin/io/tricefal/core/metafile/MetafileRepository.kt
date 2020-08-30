package io.tricefal.core.metafile

import io.tricefal.core.storage.FileStorage
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Repository
import java.io.InputStream
import java.nio.file.Paths
import java.util.*

@Repository
@PropertySource("storage.yml")
class MetafileRepository(val repository: MetafileJpaRepository, private final val env: Environment): IMetafileAdapter {

    private val dataFilesPath = env.getProperty("data.files.path")!!

    override fun save(metafile: MetafileDomain, inpputStream: InputStream) {
        val metafileEntity = repository.save(toEntity(metafile))
        val filename = "${metafileEntity.username}-${metafileEntity.id}-${metafileEntity.filename}"
        FileStorage().save(inpputStream, Paths.get("$dataFilesPath/${filename}"))
    }

    override fun findById(id: Long): Optional<MetafileDomain> {
        return repository.findById(id).map {
            fromEntity(it)
        }
    }

    override fun findByFilename(filename: String): List<MetafileDomain> {
        return repository.findByFilename(filename).map {
            fromEntity(it)
        }
    }

    override fun findByUsername(username: String): List<MetafileDomain> {
        return repository.findByUsername(username).map {
            fromEntity(it)
        }
    }
}
