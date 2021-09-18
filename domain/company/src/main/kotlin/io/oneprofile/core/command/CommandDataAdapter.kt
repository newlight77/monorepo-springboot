package io.oneprofile.core.command

import java.util.*

interface CommandDataAdapter {
    fun create(command: CommandDomain): CommandDomain
    fun update(command: CommandDomain): CommandDomain
    fun findByName(companyName: String): Optional<CommandDomain>
    fun findAll(): List<CommandDomain>
}