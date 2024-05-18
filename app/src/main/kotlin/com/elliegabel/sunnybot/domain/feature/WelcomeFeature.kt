package com.elliegabel.sunnybot.domain.feature

import com.elliegabel.sunnybot.domain.AppFeature
import com.elliegabel.sunnybot.domain.FeatureProperties
import com.elliegabel.sunnybot.domain.service.DiscordService
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Member
import dev.kord.core.entity.Message
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.event.guild.MemberJoinEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sends welcome messages and reacts to introduction messages with a friendly emoji :)
 */
class WelcomeFeature(private val discordService: DiscordService) : AppFeature {
    override fun register(kord: Kord) {
        kord.on<MemberJoinEvent> {
            handleMemberJoinEvent(this)
        }

        kord.on<MessageCreateEvent> {
            handleMessageCreateEvent(this)
        }
    }

    private suspend fun handleMemberJoinEvent(event: MemberJoinEvent) {
        val server = discordService.getServer(event.guildId) ?: return
        val properties = server.featureProperties<Properties>() ?: return

        if (!properties.shouldSendWelcomeMessage() || properties.welcomeMessageChannelId == null) {
            return
        }

        val channel = event.guild.getChannelOrNull(Snowflake(properties.welcomeMessageChannelId))
        if (channel == null) {
            server.logger.w { "Welcome message channel does not exist: '${properties.welcomeMessageChannelId}'" }
            return
        }

        if (channel !is TextChannel) {
            server.logger.w { "cannot send message to channel ${channel.id} of type ${channel.type}" }
            return
        }

        val finalMessage = properties.welcomeMessage?.formatWelcomeMessage(event.member) ?: return
        discordService.message(server, channel, finalMessage)
    }

    private suspend fun handleMessageCreateEvent(event: MessageCreateEvent) {
        if (event.message.content.isEmpty()) return

        val server = discordService.getServer(event.guildId) ?: return
        val properties = server.featureProperties<Properties>() ?: return

        if (!properties.shouldWaveInChannel(event.message)) {
            return
        }

        val firstWord = event.message.content.split(' ')[0]
        if (!GREETING_MATCHER.matches(firstWord)) {
            return
        }

        discordService.react(server, event.message, REACTION_WAVE)
    }

    private fun Properties.shouldSendWelcomeMessage(): Boolean {
        return welcomeMessageEnabled && welcomeMessageChannelId != null && !welcomeMessage.isNullOrBlank()
    }

    private fun Properties.shouldWaveInChannel(message: Message): Boolean {
        return introductionWaveEnabled && message.channelId.value.toLong() == introductionWaveChannelId
    }

    private fun String.formatWelcomeMessage(member: Member): String {
        return replace(PLACEHOLDER_MENTION, member.mention)
    }

    @Serializable
    @SerialName("WelcomeFeature")
    data class Properties(
        val introductionWaveEnabled: Boolean = false,
        val introductionWaveChannelId: Long? = null,
        val welcomeMessageEnabled: Boolean = false,
        val welcomeMessageChannelId: Long? = null,
        val welcomeMessage: String? = null,
    ) : FeatureProperties

    companion object {
        const val PLACEHOLDER_MENTION = "{mention}"
        val GREETING_MATCHER = Regex("^(hello|hi|hey|hallo|henlo|yo)$", RegexOption.IGNORE_CASE)
        val REACTION_WAVE = ReactionEmoji.Unicode("\uD83D\uDC4B")
    }
}
