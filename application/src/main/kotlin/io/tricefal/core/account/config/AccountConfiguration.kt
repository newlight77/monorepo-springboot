package io.tricefal.core.account.config

import io.tricefal.core.account.api.IAccountRepository
import io.tricefal.core.account.api.IAccountService
import io.tricefal.core.account.domain.AccountDomain
import io.tricefal.core.account.service.AccountService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AccountConfiguration {

    @Bean
    fun accountService(accountRepository: IAccountRepository<AccountDomain, Long>): IAccountService<AccountDomain, Long> {
        return AccountService(accountRepository)
    }
}