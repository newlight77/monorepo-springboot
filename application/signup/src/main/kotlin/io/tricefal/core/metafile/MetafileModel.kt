package io.tricefal.core.metafile

import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MetafileModel(val username: String,
                    val filename: String,
                    val representation: Representation,
                    val contentType: String? = null,
                    val size: Long? = null,
                    val creationDate: Instant? = Instant.now()) {
    data class Builder (
            val username: String,
            val filename: String,
            var representation: Representation,
            var contentType: String? = null,
            var size: Long? = null
    ) {

        fun contentType(contentType: String?) = apply { this.contentType = contentType }
        fun size(size: Long?) = apply { this.size = size }

        fun build(): MetafileModel {
            if (this.filename.contains("..")) {
                throw IllegalArgumentException("Sorry! Filename contains invalid path sequence $this.fileName")
            }
            return MetafileModel(username, filename, representation, contentType, size)
        }
    }
}

fun toModel(domain: MetafileDomain): MetafileModel {
    return MetafileModel(
            username = domain.username,
            filename = domain.filename,
            representation = domain.representation,
            contentType = domain.contentType,
            size = domain.size,
            creationDate = domain.creationDate
    )
}

fun fromModel(model: MetafileModel): MetafileDomain {
    return MetafileDomain(
            username = model.username,
            filename = model.filename,
            representation = model.representation,
            contentType = model.contentType,
            size = model.size,
            creationDate = model.creationDate
    )
}

fun originalFilename(fullPath: String?): String? {
    return fullPath?.substringAfterLast("/")
}

fun toMetafile(username: String, file: MultipartFile, dataFilesPath: String, representation: Representation): MetafileModel {
    val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
    val fileName: String = StringUtils.cleanPath("${dataFilesPath}/${username}/${representation}/${timestamp}-_-${file.originalFilename!!}")
    return MetafileModel.Builder(username, fileName, representation)
            .contentType(file.contentType)
            .size(file.size)
            .build()
}