package com.elliegabel.sunnybot.domain.feature

import com.elliegabel.sunnybot.config.SerializersModule
import com.elliegabel.sunnybot.domain.GuildSettings
import com.elliegabel.sunnybot.domain.feature.model.AppFeature
import com.elliegabel.sunnybot.domain.feature.model.FeatureProperties
import com.elliegabel.sunnybot.domain.service.GuildSettingsService
import dev.kord.common.entity.Permission
import dev.kord.core.Kord
import dev.kord.core.behavior.reply
import dev.kord.core.entity.Member
import dev.kord.core.entity.Message
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Stops invite links from being posted from authenticated users.
 */
class NoInvitesFeature(
    private val settings: GuildSettingsService,
    private val moderationFeature: ModerationFeature,
) : AppFeature {
    override suspend fun register(kord: Kord) {
        kord.on<MessageCreateEvent> {
            handleMessageCreate(this)
        }
    }

    private suspend fun handleMessageCreate(event: MessageCreateEvent) {
        val member = event.member ?: return

        if (!member.shouldModerate()) {
            return
        }

        val guild = settings.getGuildSettings(event.guildId) ?: return

        val properties = guild.featureProperties<Properties>() ?: return
        if (!properties.enabled) {
            return
        }

        val message = event.message
        if (!message.content.shouldFilter(properties)) {
            return
        }

        guild.logger.i { "Message ${message.content} contains invite" }

        message.replyAndDelete().also {
            logDeletion(event.kord, guild, message)
        }
    }

    private suspend fun logDeletion(
        kord: Kord,
        guildSettings: GuildSettings,
        violation: Message,
    ) {
        moderationFeature.logAutoDeletedMessage(kord, guildSettings, violation, "has invite")
    }

    /**
     * Check if we should moderate this user.
     *
     * They must not be a bot and not have ManageMessages permission.
     */
    private suspend fun Member.shouldModerate(): Boolean {
        return !isBot && !getPermissions().values.contains(Permission.ManageMessages)
    }

    private suspend fun Message.replyAndDelete() {
        this.reply { content = "Sorry, no invite links!" }
        this.delete(reason = "contains non-whitelisted invite link")
    }

    /**
     * Check if we should moderate this message.
     */
    private fun String.shouldFilter(properties: Properties): Boolean {
        if (isNullOrBlank()) {
            return false
        }

        val blacklistRegex = properties.blacklistRegex ?: return false
        val matches = blacklistRegex.findAll(this)

        return matches.any { !properties.whitelistedMatches.contains(it.value) }
    }

    @Serializable
    @SerialName("NoInvitesFeature")
    data class Properties(
        val enabled: Boolean = true,
        @Serializable(SerializersModule.RegexSerializer::class)
        val blacklistRegex: Regex? = null,
        val whitelistedMatches: List<String> = listOf(),
    ) : FeatureProperties
}
