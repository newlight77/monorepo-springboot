package io.tricefal.core.metafile

import io.tricefal.core.storage.FileStorage
import org.springframework.stereotype.Repository
import java.io.InputStream
import java.nio.file.Paths
import java.time.Instant
import java.util.*

@Repository
class MetafileRepositoryAdapter(val repository: MetafileJpaRepository): IMetafileDataAdapter {

    override fun save(metafile: MetafileDomain, inpputStream: InputStream) {
        val entity = toEntity(metafile)
        entity.creationDate = metafile.creationDate ?: Instant.now()
        val metafileEntity = repository.save(toEntity(metafile))
        FileStorage().save(inpputStream, Paths.get(metafileEntity.filename))
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
