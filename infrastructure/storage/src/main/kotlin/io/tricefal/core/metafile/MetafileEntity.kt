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

        @Column(name = "filename", length = 50)
        val filename: String,

        @Column(name = "type", length = 50)
        val type: String,

        @Column(name = "creation_date")
        var creationDate: Instant)


fun toEntity(domain: MetafileDomain): MetafileEntity {
        return MetafileEntity(
                domain.id,
                domain.username,
                domain.filename,
                domain.type,
                domain.creationDate
        )
}

fun fromEntity(entity: MetafileEntity): MetafileDomain {
        return MetafileDomain(
                entity.id,
                entity.username,
                entity.filename,
                entity.type,
                entity.creationDate
        )
}