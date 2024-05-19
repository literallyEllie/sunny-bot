package com.elliegabel.sunnybot.domain.feature.command

import com.elliegabel.sunnybot.domain.feature.model.Command
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on

/**
 * Command to return either Heads or Tails
 */
class CommandCoinFlip : Command {
    override val name: String
        get() = "coinflip"
    override val description: String
        get() = "Decide with a Head or Tails"

    override suspend fun register(kord: Kord) {
        // Command
        kord.createGlobalChatInputCommand(name, description)

        // Response
        kord.on<GuildChatInputCommandInteractionCreateEvent> {
            handleResponse(this)
        }
    }

    private suspend fun handleResponse(event: GuildChatInputCommandInteractionCreateEvent) {
        val command = event.interaction.command
        if (!shouldHandle(command)) {
            return
        }

        val response = event.interaction.deferPublicResponse()

        val heads = intArrayOf(1, 2).random() == 1
        response.respond { content = ":coin: **${if (heads) "Heads" else "Tails"}**" }
    }
}
