package io.tricefal.core.login

import io.tricefal.core.login.entity.LoginEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "user", path = "users")
interface LoginJpaRepository : JpaRepository<LoginEntity, Long> {
    fun save(entity: LoginEntity)
    @Query("SELECT t FROM LoginEntity t where t.username like %:username%")
    fun findByUsername(username: String): List<LoginEntity>
}




