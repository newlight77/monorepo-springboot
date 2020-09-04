package io.tricefal.core.metafile

import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.time.Instant

class MetafileModel(val id: Long,
                    val username: String,
                    val filename: String,
                    val type: String,
                    val creationDate: Instant = Instant.now()) {
    data class Builder (
            val id: Long,
            var username: String? = null,
            var filename: String? = null,
            var type: String? = null) {

        fun username(username: String) = apply { this.username = username }
        fun filename(filename: String) = apply { this.filename = filename }
        fun type(type: String) = apply { this.type = type }

        fun build(): MetafileModel {
            if (this.filename!!.contains("..")) {
                throw IllegalArgumentException("Sorry! Filename contains invalid path sequence $this.fileName")
            }
            return MetafileModel(id, username!!, filename!!, type!!)
        }
    }
}

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

fun toMetafile(username: String, file: MultipartFile): MetafileModel {
    val fileName: String = StringUtils.cleanPath(file.originalFilename!!)
    return MetafileModel.Builder(0L)
            .username(username)
            .type(file.contentType!!)
            .filename(fileName)
            .build()
}