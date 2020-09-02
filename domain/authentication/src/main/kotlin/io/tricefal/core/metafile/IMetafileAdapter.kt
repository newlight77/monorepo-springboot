package io.tricefal.core.metafile

import java.io.InputStream
import java.util.*

interface IMetafileAdapter {
    fun save(metafile: MetafileDomain, inpputStream: InputStream)
    fun findById(id: Long): Optional<MetafileDomain>
    fun findByFilename(filename: String): List<MetafileDomain>
    fun findByUsername(username: String): List<MetafileDomain>
}