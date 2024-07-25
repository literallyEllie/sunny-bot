package com.elliegabel.sunnybot.domain.service

import co.touchlab.kermit.Logger
import com.elliegabel.sunnybot.util.SnowflakeCooldown
import com.elliegabel.sunnybot.util.TokenBucket
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.asChannelOf
import dev.kord.core.entity.Message
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.entity.channel.TextChannel
import org.koin.mp.KoinPlatform

class DiscordService {
    private val tokenBuckets = mutableMapOf<Long, TokenBucket>()
    private val replyCooldowns = mutableMapOf<Long, SnowflakeCooldown>()

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
        val authorId = message.author?.id ?: return

        guildId.logger().d { "Replying to message from $authorId: $response" }

        // The idea with this is to avoid raiding... not a great history with that.
        if (!authorId.tryConsumeCooldown()) {
            guildId.logger().d { "Can't reply to $authorId, response on cooldown" }
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

    private fun Snowflake.guildTokenBucket(): TokenBucket {
        return tokenBuckets.computeIfAbsent(this.value.toLong()) { TokenBucket(maxMessageThreshold, bucketRefillRate) }
    }

    private fun Snowflake.tryConsumeToken(): Boolean {
        return guildTokenBucket().tryConsume()
    }

    private fun Snowflake.guildCooldowns(): SnowflakeCooldown {
        return replyCooldowns.computeIfAbsent(this.value.toLong()) { SnowflakeCooldown() }
    }

    private fun Snowflake.tryConsumeCooldown(): Boolean {
        return guildCooldowns().tryCooldown(this)
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
