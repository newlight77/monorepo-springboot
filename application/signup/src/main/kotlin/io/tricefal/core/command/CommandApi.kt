package io.tricefal.core.command

//import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import io.tricefal.shared.util.json.PatchOperation
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("/command")
class CommandApi(val commandWebHandler: CommandWebHandler) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody command: CommandModel): CommandModel {
        return commandWebHandler.create(command)
    }

    @RolesAllowed("ROLE_ac_tricefal_r")
    @GetMapping("{commandName}")
    @ResponseStatus(HttpStatus.OK)
    fun get(@PathVariable commandName: String): CommandModel {
        return commandWebHandler.findByName(commandName)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PutMapping("{commandName}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable commandName: String, @RequestBody command: CommandModel): CommandModel {
        return commandWebHandler.update(commandName, command)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PatchMapping("{commandName}")
    @ResponseStatus(HttpStatus.OK)
    fun patch(@PathVariable commandName: String, @RequestBody patchOperations: List<PatchOperation>): CommandModel {
        return commandWebHandler.patch(commandName, patchOperations)
    }

}