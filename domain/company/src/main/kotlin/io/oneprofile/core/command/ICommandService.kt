package io.oneprofile.core.command

import io.oneprofile.shared.util.json.PatchOperation

interface ICommandService {
    fun create(command: CommandDomain) : CommandDomain
    fun update(companyName: String, command: CommandDomain) : CommandDomain
    fun patch(companyName: String, operations: List<PatchOperation>) : CommandDomain
    fun findByName(companyName: String): CommandDomain
    fun findAll(): List<CommandDomain>
}