package io.tricefal.core.config

import io.tricefal.core.account.entity.AccountEntity
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer

@Configuration
class RestConfiguration : RepositoryRestConfigurer {
   override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration?) {
        config?.exposeIdsFor(AccountEntity::class.java)
       config?.setBasePath("/rest")
   }
}