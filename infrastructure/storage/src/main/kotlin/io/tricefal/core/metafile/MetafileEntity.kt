package io.tricefal.core.metafile

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "meta_file")
data class MetafileEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        val id: Long,

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
        var creationDate: Instant? = null)


fun toEntity(domain: MetafileDomain): MetafileEntity {
        return MetafileEntity(
                0L,
                domain.username,
                domain.filename,
                domain.representation.toString(),
                domain.contentType,
                domain.size,
                domain.creationDate
        )
}

fun fromEntity(entity: MetafileEntity): MetafileDomain {
        return MetafileDomain(
                entity.username,
                entity.filename,
                Representation.valueOf(entity.representation),
                entity.contentType,
                entity.size,
                entity.creationDate
        )
}