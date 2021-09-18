package io.oneprofile.core.metafile

import io.oneprofile.core.storage.FileStorage
import org.springframework.stereotype.Repository
import java.io.InputStream
import java.nio.file.Paths
import java.time.Instant
import java.util.*

@Repository
class MetafileRepositoryAdapter(val repository: MetafileJpaRepository): IMetafileDataAdapter {

    override fun save(metafile: MetafileDomain, inpputStream: InputStream): MetafileDomain {
        var newEntity = toEntity(metafile)
        repository.findByUsername(metafile.username, metafile.representation.toString()).stream().findFirst().ifPresent {
            newEntity.id = it.id
            newEntity.creationDate = it.creationDate ?: Instant.now()
            newEntity = repository.save(newEntity)
        }

        newEntity = repository.save(newEntity)
        FileStorage().save(inpputStream, Paths.get(newEntity.filename))
        return fromEntity(newEntity)
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

    override fun findByUsername(username: String, representation: Representation): List<MetafileDomain> {
        return repository.findByUsername(username, representation.toString()).map {
            fromEntity(it)
        }
    }
}
