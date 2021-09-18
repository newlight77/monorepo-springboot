package io.oneprofile.core.metafile

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface MetafileJpaRepository : JpaRepository<MetafileEntity, Long> {
    fun save(entity: MetafileEntity): MetafileEntity
    fun findById(username: String): List<MetafileEntity>

    @Query("SELECT t FROM MetafileEntity t where t.username like %:username% ORDER by t.creationDate DESC")
    fun findByUsername(username: String): List<MetafileEntity>

    @Query("SELECT t FROM MetafileEntity t where t.username like %:username% and t.representation = :representation ORDER by t.creationDate DESC")
    fun findByUsername(username: String, representation: String): List<MetafileEntity>

    @Query("SELECT t FROM MetafileEntity t where t.filename like %:filename%  ORDER by t.creationDate DESC")
    fun findByFilename(filename: String): List<MetafileEntity>

}
