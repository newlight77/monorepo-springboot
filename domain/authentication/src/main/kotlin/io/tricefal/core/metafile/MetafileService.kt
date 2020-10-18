package io.tricefal.core.metafile

import java.io.InputStream
import java.util.*

class MetafileService(private var adapter: IMetafileAdapter) : IMetafileService {
    override fun save(metafile: MetafileDomain, inpputStream: InputStream) {
        this.adapter.save(metafile, inpputStream)
    }

    override fun findById(id: Long): Optional<MetafileDomain> {
        return this.adapter.findById(id)
    }

    override fun findByFilename(filename: String): List<MetafileDomain> {
        return this.adapter.findByFilename(filename)
    }

    override fun findByUsername(username: String): List<MetafileDomain> {
        return this.adapter.findByUsername(username)
    }

}
