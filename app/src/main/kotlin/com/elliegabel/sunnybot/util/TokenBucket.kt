package com.elliegabel.sunnybot.util

/**
 * Ratelimiter for an entity.
 *
 * For example, if the [capacity] is 10, and [refillRate] is 1.0,
 * 10 tokens can be used per second.
 */
class TokenBucket(private val capacity: Int, private val refillRate: Double) {
    private var tokens = capacity.toDouble()
    private var lastRefillTime = System.currentTimeMillis()

    @Synchronized
    fun tryConsume(count: Int = 1): Boolean {
        refillTokens()
        if (tokens >= count) {
            tokens -= count.toDouble()
            return true
        }
        return false
    }

    @Synchronized
    private fun refillTokens() {
        val now = System.currentTimeMillis()
        val elapsedTime = now - lastRefillTime
        val tokensToAdd = elapsedTime / 1000.0 * refillRate
        tokens = (tokens + tokensToAdd).coerceAtMost(capacity.toDouble())
        lastRefillTime = now
    }
}
