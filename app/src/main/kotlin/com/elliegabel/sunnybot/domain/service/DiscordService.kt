package com.elliegabel.sunnybot.domain.service

import co.touchlab.kermit.Logger
import com.elliegabel.sunnybot.config.SerializersModule
import com.elliegabel.sunnybot.domain.Server
import com.elliegabel.sunnybot.domain.Servers
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.asChannelOf
import dev.kord.core.entity.Message
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.entity.channel.TextChannel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.koin.mp.KoinPlatform

class DiscordService {
    private val json =
        Json {
            serializersModule = SerializersModule.featurePropertiesModule
        }

    private val servers: Map<Long, Server> = loadServers()

    suspend fun message(
        server: Server,
        channel: TextChannel,
        message: String,
    ) {
        server.logger.d { "Sending message to ${channel.id}: $message" }

        // The idea with this is to avoid raiding... not a great history with that.
        if (!server.tokenBucket.tryConsume()) {
            server.logger.w { "Message rate-limited ($maxMessageThreshold reached within $bucketRefillRate)" }
            return
        }

        channel.asChannelOf<TextChannel>().createMessage(message)
    }

    suspend fun reply(
        server: Server,
        message: Message,
        response: String,
    ) {
        server.logger.d { "Replying to message from ${message.author?.id}: $response" }

        // The idea with this is to avoid raiding... not a great history with that.
        if (!server.tokenBucket.tryConsume()) {
            server.logger.w { "Message rate-limited ($maxMessageThreshold reached within $bucketRefillRate)" }
            return
        }

        message.channel.createMessage(response)
    }

    suspend fun react(
        server: Server,
        message: Message,
        reactionEmoji: ReactionEmoji,
    ) {
        server.logger.d { "Reacting to message from ${message.author?.id}: ${reactionEmoji.name}" }

        if (!server.tokenBucket.tryConsume()) {
            server.logger.w { "Reaction rate-limited ($maxMessageThreshold reached within $bucketRefillRate)" }
            return
        }

        message.addReaction(reactionEmoji)
    }

    fun getServer(guildId: Snowflake?): Server? {
        if (guildId == null) {
            return null
        }

        return servers[guildId.value.toLong()]
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun loadServers(): Map<Long, Server> {
        val channelStream =
            DiscordService::class.java.classLoader.getResourceAsStream(SERVER_DEFINITIONS) ?: return mapOf()

        val stream = json.decodeFromStream<Servers>(channelStream).servers
        Logger.withTag("bootstrap").i { "Loaded ${servers.size} server definitions" }

        return stream.associateBy { it.id }
    }

    companion object {
        /**
         * All the servers the bot will serve.
         */
        private const val SERVER_DEFINITIONS = "servers.json"

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
