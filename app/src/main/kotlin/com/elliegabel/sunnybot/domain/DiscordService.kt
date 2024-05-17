package com.elliegabel.sunnybot.domain

import co.touchlab.kermit.Logger
import com.elliegabel.sunnybot.util.TokenBucket
import dev.kord.core.entity.Message
import org.koin.mp.KoinPlatform

class DiscordService {

    private val tokenBucket = TokenBucket(capacity = maxMessageThreshold, refillRate = bucketRefillRate)

    suspend fun reply(message: Message, response: String) {
        Logger.i { "Replying to message from ${message.author?.id}: $response" }

        // The idea with this is to avoid raiding... not a great history with that.
        if (!tokenBucket.tryConsume()) {
            Logger.i { "Message ratelimited ($maxMessageThreshold reached within $bucketRefillRate)" }
            return
        }

        message.channel.createMessage(response)
    }

    private companion object {

        /**
         * Max messages to reach within a timeframe before the messageCooldown is applied.
         */
        private val maxMessageThreshold: Int =
            KoinPlatform.getKoin().getProperty("maxInputCapacity", "10").toInt()

        /**
         * Period to sample messages to compare for the maxMessageThreshold
         */
        private val bucketRefillRate: Double =
            KoinPlatform.getKoin().getProperty("inputBucketRefillRate", "1.0").toDouble()
    }
}