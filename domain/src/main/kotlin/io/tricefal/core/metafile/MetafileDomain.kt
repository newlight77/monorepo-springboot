package io.tricefal.core.metafile

import java.time.Instant

class MetafileDomain(val id: Long,
                 val username: String,
                 val filename: String,
                 val type: String,
                 val creationDate: Instant = Instant.now())