package com.elliegabel.sunnybot.domain.feature.command

import com.elliegabel.sunnybot.domain.feature.model.Command
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.string

/**
 * Responds to a question with 1 of 20 baseless answers.
 */
class Command8Ball : Command {
    override val name: String
        get() = "8ball"
    override val description: String
        get() = "Predict the future"
    private val responses =
        listOf(
            // Positive
            "It is certain.", "It is decidedly so.", "Without a doubt.", "Yes – definitely.",
            "You may rely on it.", "As I see it, yes.", "Most likely.", "Outlook good.", "Yes.", "Signs point to yes.",
            // Negative
            "Don’t count on it.", "My reply is no.", "My sources say no.", "Outlook not so good.", "Very doubtful.",
            // Neutral
            "... ur mom?", "Ask again later.", "Better not tell you now.", "Cannot predict now.",
            "Concentrate and ask again.",
        )
    private val parameterQuestion = "question"

    override suspend fun register(kord: Kord) {
        // Command
        kord.createGlobalChatInputCommand(name, description) {
            string(parameterQuestion, "Your Question") {
                required = true
            }
        }

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

        // TODO vulnerable to explicit questions
        val randomResponse = responses.random()
        response.respond { content = randomResponse }
    }
}
