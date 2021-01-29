package io.tricefal.core.metafile

import java.io.InputStream
import java.util.*

class MetafileService(private var dataAdapter: IMetafileDataAdapter) : IMetafileService {
    override fun save(metafile: MetafileDomain, inpputStream: InputStream): MetafileDomain {
        return this.dataAdapter.save(metafile, inpputStream)
    }

    override fun findById(id: Long): Optional<MetafileDomain> {
        return this.dataAdapter.findById(id)
    }

    override fun findByFilename(filename: String): List<MetafileDomain> {
        return this.dataAdapter.findByFilename(filename)
    }

    override fun findByUsername(username: String): List<MetafileDomain> {
        return this.dataAdapter.findByUsername(username)
    }

    override fun findByUsername(username: String, representation: Representation): List<MetafileDomain> {
        return this.dataAdapter.findByUsername(username, representation)
    }

}
