package io.oneprofile.core.command

import io.oneprofile.core.exception.GlobalNotFoundException
import io.oneprofile.core.metafile.IMetafileService
import io.oneprofile.shared.util.json.PatchOperation
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
@PropertySource("classpath:application.yml")
class CommandWebHandler(val commandService: ICommandService,
                        val metafileService: IMetafileService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun create(command: CommandModel): CommandModel {
        val domain = fromModel(command)
        val result = try {
            commandService.create(domain)
        } catch (ex: Throwable) {
            throw FreelanceCreationException("Failed to create a command with companyName ${command.companyName}", ex)
        }
        return toModel(result)
    }

    fun update(name: String, command: CommandModel): CommandModel {
        val result = try {
            val domain = fromModel(command)
            commandService.update(name, domain)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("command not found with companyName $name", ex)
        } catch (ex: Throwable) {
            throw FreelanceUpdateException("Failed to update a command with companyName $name with $command", ex)
        }
        return toModel(result)
    }

    fun patch(name: String, operations: List<PatchOperation>): CommandModel {
        val result = try {
            commandService.patch(name, operations)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("command not found with companyName $name", ex)
        } catch (ex: Throwable) {
            throw FreelanceUpdateException("Failed to create a command with companyName $name with operations ${operations.joinToString()}", ex)
        }
        return toModel(result)
    }

    fun findByName(commandName: String): CommandModel {
        if (commandName.isEmpty()) throw throw ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "command not found with companyName $commandName")
        val domain = try {
            this.commandService.findByName(commandName)
        } catch (ex: Throwable) {
            throw GlobalNotFoundException("Failed to find a command with companyName $commandName", ex)
        }
        return toModel(domain)
    }

    class FreelanceCreationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class FreelanceUpdateException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
}