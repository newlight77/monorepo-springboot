package io.tricefal.core.account.jpa

import io.tricefal.core.account.entity.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.util.*

@RepositoryRestResource(collectionResourceRel = "account", path = "accounts")
interface AccountJpaRepository : JpaRepository<AccountEntity, Long> {
    fun save(entity: AccountEntity)
    override fun delete(entity: AccountEntity)
    override fun findAll(): List<AccountEntity>
    override fun findById(id: Long): Optional<AccountEntity>
    @Query("SELECT t FROM AccountEntity t where t.username like %:username%")
    fun findByUsername(username: String): List<AccountEntity>
    @Query("SELECT t FROM AccountEntity t, ContactEntity c where t.id = c.id and c.email like %:email%")
    fun findByEmail(email: String): List<AccountEntity>
}




