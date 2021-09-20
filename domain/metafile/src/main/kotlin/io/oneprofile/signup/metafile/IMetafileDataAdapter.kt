package io.oneprofile.signup.metafile

import java.io.InputStream
import java.util.*

interface IMetafileDataAdapter {
    fun save(metafile: MetafileDomain, inpputStream: InputStream): MetafileDomain
    fun findById(id: Long): Optional<MetafileDomain>
    fun findByFilename(filename: String): List<MetafileDomain>
    fun findByUsername(username: String): List<MetafileDomain>
    fun findByUsername(username: String, representation: Representation): List<MetafileDomain>
}