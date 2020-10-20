package io.tricefal.core.metafile

import java.time.Instant

enum class Representation {
    PORTRAIT,
    CV,
    REF,
    KBIS,
    RIB,
    RC,
    URSSAF,
    FISCAL
}

class MetafileDomain(val username: String,
                 val filename: String,
                 val contentType: String,
                 val size: Long,
                 val representation: Representation,
                 val creationDate: Instant = Instant.now())