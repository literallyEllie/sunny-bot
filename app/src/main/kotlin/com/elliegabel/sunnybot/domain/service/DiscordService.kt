package com.elliegabel.sunnybot.domain.service

import co.touchlab.kermit.Logger
import com.elliegabel.sunnybot.domain.feature.model.AppFeature
import com.elliegabel.sunnybot.util.TokenBucket
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.asChannelOf
import dev.kord.core.entity.Message
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.entity.channel.TextChannel
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import org.koin.mp.KoinPlatform

class DiscordService {
    private lateinit var kord: Kord
    private val tokenBuckets = mutableMapOf<Long, TokenBucket>()

    @OptIn(PrivilegedIntent::class)
    suspend fun initialise() {
        kord =
            Kord(
                KoinPlatform.getKoin().getProperty("DISCORD_TOKEN")
                    ?: throw NullPointerException("Bot token required"),
            )

        // Init features
        val features: List<AppFeature> = KoinPlatform.getKoin().getAll()
        features.forEach { it.register(kord) }

        kord.login {
            intents += Intent.MessageContent
            intents += Intent.GuildMembers
        }
    }

    suspend fun message(
        channel: TextChannel,
        message: String,
    ) {
        val guild = channel.guildId
        guild.logger().d { "Sending message to ${channel.id}: $message" }

        // The idea with this is to avoid raiding... not a great history with that.
        if (!guild.tryConsumeToken()) {
            guild.logger().w { "Message rate-limited ($maxMessageThreshold reached within $bucketRefillRate)" }
            return
        }

        channel.asChannelOf<TextChannel>().createMessage(message)
    }

    suspend fun reply(
        guildId: Snowflake?,
        message: Message,
        response: String,
    ) {
        guildId ?: return

        guildId.logger().d { "Replying to message from ${message.author?.id}: $response" }

        // The idea with this is to avoid raiding... not a great history with that.
        if (!guildId.tryConsumeToken()) {
            guildId.logger().w { "Message rate-limited ($maxMessageThreshold reached within $bucketRefillRate)" }
            return
        }

        message.channel.createMessage(response)
    }

    suspend fun react(
        guildId: Snowflake?,
        message: Message,
        reactionEmoji: ReactionEmoji,
    ) {
        guildId ?: return

        guildId.logger().d { "Reacting to message from ${message.author?.id}: ${reactionEmoji.name}" }

        if (!guildId.tryConsumeToken()) {
            guildId.logger().w { "Reaction rate-limited ($maxMessageThreshold reached within $bucketRefillRate)" }
            return
        }

        message.addReaction(reactionEmoji)
    }

    private fun Snowflake.bucket(): TokenBucket {
        return tokenBuckets.computeIfAbsent(this.value.toLong()) { TokenBucket(maxMessageThreshold, bucketRefillRate) }
    }

    private fun Snowflake.tryConsumeToken(): Boolean {
        return bucket().tryConsume()
    }

    private fun Snowflake.logger(): Logger = Logger.withTag(this.value.toString())

    companion object {
        /**
         * Max messages to reach within a timeframe before the messageCooldown is applied.
         */
        val maxMessageThreshold: Int =
            KoinPlatform.getKoin().getProperty("maxInputCapacity", "10").toInt()

        /**
         * Period to sample messages to compare for the maxMessageThreshold
         */
        val bucketRefillRate: Double =
            KoinPlatform.getKoin().getProperty("inputBucketRefillRate", "1.0").toDouble()
    }
}
