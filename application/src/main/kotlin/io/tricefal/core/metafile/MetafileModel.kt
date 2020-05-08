package io.tricefal.core.metafile

import java.time.Instant

class MetafileModel(val id: Long,
                    val username: String,
                    val filename: String,
                    val type: String,
                    val creationDate: Instant = Instant.now())

fun toModel(domain: MetafileDomain): MetafileModel {
    return MetafileModel(
            domain.id,
            domain.username,
            domain.filename,
            domain.type,
            domain.creationDate
    )
}

fun fromModel(model: MetafileModel): MetafileDomain {
    return MetafileDomain(
            model.id,
            model.username,
            model.filename,
            model.type,
            model.creationDate
    )
}