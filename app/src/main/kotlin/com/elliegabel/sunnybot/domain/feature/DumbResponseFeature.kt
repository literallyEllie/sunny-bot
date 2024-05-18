package com.elliegabel.sunnybot.domain.feature

import co.touchlab.kermit.Logger
import com.elliegabel.sunnybot.domain.AppFeature
import com.elliegabel.sunnybot.domain.FeatureProperties
import com.elliegabel.sunnybot.domain.service.DiscordService
import com.elliegabel.sunnybot.ext.isBot
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

/**
 * Responds to regex messages with a simple response.
 */
class DumbResponseFeature(private val discordService: DiscordService) : AppFeature {
    private val responses: Map<Regex, String> = loadResponses()
    private val lastResponseTime = mutableMapOf<Regex, Long>()

    override fun register(kord: Kord) {
        kord.on<MessageCreateEvent> {
            handleMessageCreate(this)
        }
    }

    private suspend fun handleMessageCreate(event: MessageCreateEvent) {
        if (event.message.isBot()) {
            return
        }

        // Only have this in GENERAL
        val server = discordService.getServer(event.guildId) ?: return
        val properties = server.featureProperties<Properties>() ?: return

        if (!properties.shouldReply(event.message.channelId)) {
            return
        }

        val content = event.message.content
        if (content.isEmpty()) {
            return
        }

        server.logger.d { "Looking for a dumb response to '$content'" }
        val response = getResponse(content) ?: return

        if (responseOnCooldown(response.key)) {
            server.logger.d { "Dumb response '${response.key}' is on cooldown " }
            return
        }

        markLastResponseTime(response.key)
        discordService.reply(server, event.message, response.value)
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

    private fun Properties.shouldReply(channel: Snowflake): Boolean {
        return enabled && channels.contains(channel.value.toLong())
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun loadResponses(): Map<Regex, String> {
        val resourceStream = DumbResponseFeature::class.java.classLoader.getResourceAsStream(RESPONSES_FILE)
        if (resourceStream == null) {
            Logger.w { "$RESPONSES_FILE does not exist" }
            return mapOf()
        }

        return Json.decodeFromStream<Map<String, String>>(resourceStream).mapKeys { Regex(it.key) }.also {
            Logger.withTag("bootstrap").i { "Loaded ${it.size} dumb responses" }
        }
    }

    @Serializable
    @SerialName("DumbResponder")
    data class Properties(
        val enabled: Boolean = false,
        val channels: Set<Long> = setOf(),
    ) : FeatureProperties

    private companion object {
        const val RESPONSES_FILE = "responses.json"
        const val MESSAGE_COOLDOWN_MILLIS = 5000
    }
}
