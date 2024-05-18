package com.elliegabel.sunnybot.domain

import co.touchlab.kermit.Logger
import com.elliegabel.sunnybot.domain.service.DiscordService.Companion.bucketRefillRate
import com.elliegabel.sunnybot.domain.service.DiscordService.Companion.maxMessageThreshold
import com.elliegabel.sunnybot.util.TokenBucket
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Servers(val servers: List<Server>)

@Serializable
data class Server(val id: Long, val features: Set<FeatureProperties>) {
    @Transient
    val tokenBucket =
        TokenBucket(
            capacity = maxMessageThreshold,
            refillRate = bucketRefillRate,
        )

    internal val logger: Logger
        get() = Logger.withTag(id.toString())

    inline fun <reified T : FeatureProperties> featureProperties(): T? {
        return features.filterIsInstance<T>().firstOrNull()
    }
}
