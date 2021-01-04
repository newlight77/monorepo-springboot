package io.tricefal.core.metafile

import io.tricefal.core.metafile.MetafileDomain
import java.io.InputStream
import java.util.*

interface IMetafileService {
    fun save(metafile: MetafileDomain, inpputStream: InputStream): MetafileDomain
    fun findById(id: Long): Optional<MetafileDomain>
    fun findByFilename(filename: String): List<MetafileDomain>
    fun findByUsername(username: String): List<MetafileDomain>
}