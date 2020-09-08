package io.tricefal.core.metafile

import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.time.Instant

class MetafileModel(val username: String,
                    val filename: String,
                    val type: String,
                    val creationDate: Instant = Instant.now()) {
    data class Builder (
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
            return MetafileModel(username!!, filename!!, type!!)
        }
    }
}

fun toModel(domain: MetafileDomain): MetafileModel {
    return MetafileModel(
            domain.username,
            domain.filename,
            domain.type,
            domain.creationDate
    )
}

fun fromModel(model: MetafileModel): MetafileDomain {
    return MetafileDomain(
            model.username,
            model.filename,
            model.type,
            model.creationDate
    )
}

fun toMetafile(username: String, file: MultipartFile): MetafileModel {
    val originalFilename: String = StringUtils.cleanPath(file.originalFilename!!)
    val filename = "${username}-${randomString()}-${originalFilename}"
    return MetafileModel.Builder()
            .username(username)
            .type(file.contentType!!)
            .filename(filename)
            .build()
}

fun randomString(): String {
    val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..10)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
}