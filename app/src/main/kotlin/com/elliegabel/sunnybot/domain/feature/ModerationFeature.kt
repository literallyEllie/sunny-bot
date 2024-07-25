package com.elliegabel.sunnybot.domain.feature

import com.elliegabel.sunnybot.domain.GuildSettings
import com.elliegabel.sunnybot.domain.feature.model.AppFeature
import com.elliegabel.sunnybot.domain.feature.model.FeatureProperties
import com.elliegabel.sunnybot.domain.service.DiscordService
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Message
import dev.kord.core.entity.channel.TextChannel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * General moderation settings.
 */
class ModerationFeature(
    private val discordService: DiscordService,
) : AppFeature {
    override suspend fun register(kord: Kord) {
        // NOOP
    }

    suspend fun logAutoDeletedMessage(
        kord: Kord,
        guild: GuildSettings,
        message: Message,
        reason: String,
    ) {
        logAutoModAction(
            kord,
            guild,
            "Deleted message from ${message.author?.mention} because **$reason**: ${message.content}",
        )
    }

    suspend fun logAutoModAction(
        kord: Kord,
        guild: GuildSettings,
        message: String,
    ) {
        val properties = guild.featureProperties<Properties>() ?: return
        if (!properties.logAutoMod) {
            return
        }

        val channelId = properties.logChannelId?.snowflake() ?: return
        val channel = kord.getChannel(channelId)
        if (channel == null) {
            guild.logger.w { "auto mod log channel does not exist $channelId" }
            return
        }

        if (channel !is TextChannel) {
            guild.logger.w { "auto mod log channel is not TextChannel $channelId" }
            return
        }

        discordService.message(channel, "**AutoMod** :sunflower: $message")
    }

    private fun Long.snowflake(): Snowflake {
        return Snowflake(this)
    }

    @Serializable
    @SerialName("ModerationFeature")
    data class Properties(
        val logAutoMod: Boolean = true,
        val logChannelId: Long? = null,
    ) : FeatureProperties
}
