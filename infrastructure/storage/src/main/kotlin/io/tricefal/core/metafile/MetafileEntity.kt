package io.tricefal.core.metafile

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "meta_file")
data class MetafileEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long,

        @Column(name = "username", length = 50)
        val username: String,

        @Column(name = "filename", length = 255)
        val filename: String,

        @Column(name = "Representation", length = 50)
        val representation: String,

        @Column(name = "content_type", length = 30)
        var contentType: String? = null,

        @Column(name = "size")
        var size: Long? = null,

        @Column(name = "creation_date")
        var creationDate: Instant? = Instant.now())


fun toEntity(domain: MetafileDomain): MetafileEntity {
        return MetafileEntity(
                id = 0L,
                username = domain.username,
                filename = domain.filename,
                representation = domain.representation.toString(),
                contentType = domain.contentType,
                size = domain.size,
                creationDate = domain.creationDate
        )
}

fun fromEntity(entity: MetafileEntity): MetafileDomain {
        return MetafileDomain(
                username = entity.username,
                filename =  entity.filename,
                representation = Representation.valueOf(entity.representation),
                contentType = entity.contentType,
                size = entity.size,
                creationDate = entity.creationDate
        )
}