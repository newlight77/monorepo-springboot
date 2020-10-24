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
                 val representation: Representation,
                 var contentType: String? = null,
                 var size: Long? = null,
                 var creationDate: Instant? = Instant.now())