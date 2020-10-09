package io.tricefal.core.metafile

import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MetafileModel(val username: String,
                    val filename: String,
                    val contentType: String,
                    val size: Long,
                    val representation: Representation,
                    val creationDate: Instant = Instant.now()) {
    data class Builder (
            var username: String? = null,
            var filename: String? = null,
            var contentType: String? = null,
            var size: Long? = null,
            var representation: Representation? = null) {

        fun username(username: String) = apply { this.username = username }
        fun filename(filename: String) = apply { this.filename = filename }
        fun contentType(contentType: String) = apply { this.contentType = contentType }
        fun size(size: Long) = apply { this.size = size }
        fun representation(representation: Representation) = apply { this.representation = representation }

        fun build(): MetafileModel {
            if (this.filename!!.contains("..")) {
                throw IllegalArgumentException("Sorry! Filename contains invalid path sequence $this.fileName")
            }
            return MetafileModel(username!!, filename!!, contentType!!, size!!, representation!!)
        }
    }
}

fun toModel(domain: MetafileDomain): MetafileModel {
    return MetafileModel(
            domain.username,
            domain.filename,
            domain.contentType,
            domain.size,
            domain.representation,
            domain.creationDate
    )
}

fun fromModel(model: MetafileModel): MetafileDomain {
    return MetafileDomain(
            model.username,
            model.filename,
            model.contentType,
            model.size,
            model.representation,
            model.creationDate
    )
}

fun toMetafile(username: String, file: MultipartFile, dataFilesPath: String, representation: Representation): MetafileModel {
    val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MdyHmss"))
    val fileName: String = StringUtils.cleanPath("${dataFilesPath}/${username}/${representation}/${timestamp}-${file.originalFilename!!}")
    return MetafileModel.Builder()
            .username(username)
            .contentType(file.contentType!!)
            .size(file.size)
            .representation(representation)
            .filename(fileName)
            .build()
}