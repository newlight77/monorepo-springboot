package io.tricefal.core.command

import io.tricefal.core.freelance.*
import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.MetaNotificationDomain
import io.tricefal.shared.util.json.JsonPatchOperator
import io.tricefal.shared.util.json.PatchOperation
import org.slf4j.LoggerFactory
import java.text.MessageFormat
import java.time.Instant
import java.util.*

class CommandService(private var dataAdapter: CommandDataAdapter) : ICommandService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(command: CommandDomain): CommandDomain {
        val result = dataAdapter.findByName(command.companyName)
        return if (result.isPresent) dataAdapter.update(result.get())
        else dataAdapter.create(command)
    }

    override fun update(companyName: String, command: CommandDomain): CommandDomain {
        val result = dataAdapter.findByName(companyName).orElse(dataAdapter.create(command))
        dataAdapter.update(result)
        return result
    }

    override fun patch(companyName: String, operations: List<PatchOperation>): CommandDomain {
        val command = dataAdapter.findByName(companyName)
        if (command.isEmpty){
            var newCommand = createCommand(companyName)
            newCommand = applyPatch(newCommand, operations)
            dataAdapter.create(newCommand)
            return newCommand
        }
        val patched = applyPatch(command.get(), operations)
        dataAdapter.update(patched)
        return patched
    }

    private fun applyPatch(
        command: CommandDomain,
        operations: List<PatchOperation>,
    ): CommandDomain {
        if (command.address == null) command.address = AddressDomain.Builder().build()

        return operations.let { ops ->
            val patched = JsonPatchOperator().apply(command, ops)
            patched.lastDate = command.lastDate ?: Instant.now()
            patched
        }
    }

    override fun findByName(companyName: String): CommandDomain {
        val result = dataAdapter.findByName(companyName)
        return if (result.isPresent) {
            result.get()
        } else throw NotFoundException("Failed to find a command for user $companyName")
    }

    override fun findAll(): List<CommandDomain> {
        return dataAdapter.findAll()
    }

    private fun createCommand(username: String): CommandDomain {
        val address = AddressDomain.Builder().build()
        return CommandDomain.Builder(username)
            .address(address)
            .build()
    }
}

class NotFoundException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
