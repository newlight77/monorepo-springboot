package io.tricefal.core.account

import io.tricefal.core.account.api.IAccountService
import io.tricefal.core.account.model.AccountModel
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("account")
class AccountApi(val accountService: IAccountService<AccountModel, Long>) {

    @PostMapping
    fun create(@RequestBody account: AccountModel) {
        accountService.create(account)
    }

    @PutMapping("{username}")
    fun update(@PathVariable username: String, @RequestBody account: AccountModel ) {
        accountService.update(account)
    }

    @DeleteMapping("{username}")
    fun delete(@PathVariable id: Long) {
        accountService.delete(id)
    }

    @GetMapping("")
    fun find(@RequestParam username: String, @RequestParam email: String) {
        accountService.find(username, email)
    }
}