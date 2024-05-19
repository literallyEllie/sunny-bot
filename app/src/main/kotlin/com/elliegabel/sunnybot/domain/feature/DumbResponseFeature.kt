package com.elliegabel.sunnybot.domain.feature

import co.touchlab.kermit.Logger
import com.elliegabel.sunnybot.domain.feature.model.AppFeature
import com.elliegabel.sunnybot.domain.feature.model.FeatureProperties
import com.elliegabel.sunnybot.domain.service.DiscordService
import com.elliegabel.sunnybot.domain.service.GuildSettingsService
import com.elliegabel.sunnybot.ext.isBot
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Responds to regex-matched messages with a simple response.
 */
class DumbResponseFeature(
    private val discordService: DiscordService,
    private val settings: GuildSettingsService,
) : AppFeature {
    private val responses: Map<Regex, String> = loadResponses()
    private val lastResponseTime = mutableMapOf<Regex, Long>()

    override suspend fun register(kord: Kord) {
        kord.on<MessageCreateEvent> {
            handleMessageCreate(this)
        }
    }

    private suspend fun handleMessageCreate(event: MessageCreateEvent) {
        if (event.message.isBot()) {
            return
        }

        // Only have this in GENERAL
        val server = settings.getGuildSettings(event.guildId) ?: return
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
        discordService.reply(event.guildId, event.message, response.value)
    }

    /**
     * Finds a possible response to a message.
     */
    private fun getResponse(content: String): Map.Entry<Regex, String>? {
        return responses.entries.firstOrNull { it.key.matches(content) }
    }

    /**
     * Check there has been sufficient time between the last response of this type.
     */
    private fun responseOnCooldown(trigger: Regex): Boolean {
        val lastResponse = lastResponseTime[trigger] ?: return false
        return System.currentTimeMillis() - lastResponse <= MESSAGE_COOLDOWN_MILLIS
    }

    private fun markLastResponseTime(trigger: Regex) {
        lastResponseTime[trigger] = System.currentTimeMillis()
    }

    /**
     * Check if a reply should be made in the channel.
     */
    private fun Properties.shouldReply(channel: Snowflake): Boolean {
        return enabled && enabledChannels.contains(channel.value.toLong())
    }

    private fun loadResponses(): Map<Regex, String> {
        val settings = File("config", RESPONSES_FILE)
        if (!settings.isFile) {
            Logger.w { "Dumb responses do not exist: ${settings.name}" }
            return mapOf()
        }

        return Json.decodeFromString<Map<String, String>>(settings.readText()).mapKeys { Regex(it.key) }.also {
            Logger.withTag("bootstrap").i { "Loaded ${it.size} dumb responses" }
        }
    }

    @Serializable
    @SerialName("DumbResponder")
    data class Properties(
        val enabled: Boolean = false,
        val enabledChannels: Set<Long> = setOf(),
    ) : FeatureProperties

    private companion object {
        const val RESPONSES_FILE = "responses.json"
        const val MESSAGE_COOLDOWN_MILLIS = 5000
    }
}
