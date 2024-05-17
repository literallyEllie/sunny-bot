package com.elliegabel.sunnybot.features

import co.touchlab.kermit.Logger
import com.elliegabel.sunnybot.domain.AppFeature
import com.elliegabel.sunnybot.domain.DiscordService
import com.elliegabel.sunnybot.ext.isBot
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Responds to regex messages with a simple response.
 */
class DumbResponder(private val discordService: DiscordService) : AppFeature {
    private val responses: Map<Regex, String> = loadResponses()
    private val lastResponseTime = mutableMapOf<Regex, Long>()

    override fun register(kord: Kord) {
        kord.on<MessageCreateEvent> {
            if (message.isBot()) {
                return@on
            }

            val content = message.content
            Logger.d { "Looking for a dumb response to '$content'" }

            val response = getResponse(content) ?: return@on

            if (responseOnCooldown(response.key)) {
                Logger.d { "Dumb response '${response.key}' is on cooldown " }
                return@on
            }

            markLastResponseTime(response.key)
            discordService.reply(message, response.value)
        }
    }

    private fun getResponse(content: String): Map.Entry<Regex, String>? {
        return responses.entries.firstOrNull { it.key.matches(content) }
    }

    private fun responseOnCooldown(trigger: Regex): Boolean {
        val lastResponse = lastResponseTime[trigger] ?: return false
        return System.currentTimeMillis() - lastResponse <= MESSAGE_COOLDOWN_MILLIS
    }

    private fun markLastResponseTime(trigger: Regex) {
        lastResponseTime[trigger] = System.currentTimeMillis()
    }

    private fun loadResponses(): Map<Regex, String> {
        val responses = mutableMapOf<Regex, String>()

        val resourceStream = DumbResponder::class.java.classLoader.getResourceAsStream(RESPONSES_FILE)
        if (resourceStream == null) {
            Logger.w { "$RESPONSES_FILE does not exist" }
            return responses
        }

        val reader = BufferedReader(InputStreamReader(resourceStream))
        reader.forEachLine { line ->
            val (pattern, response) = line.split(RESPONSES_DELIMITER, limit = 2)

            responses[Regex(pattern)] = response
        }

        Logger.i { "Loaded ${responses.size} dumb responses" }
        return responses
    }

    private companion object {
        const val RESPONSES_FILE = "responses.txt"
        const val RESPONSES_DELIMITER = " : "
        const val MESSAGE_COOLDOWN_MILLIS = 5000
    }
}