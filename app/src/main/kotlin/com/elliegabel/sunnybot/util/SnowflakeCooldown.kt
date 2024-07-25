package com.elliegabel.sunnybot.util

import dev.kord.common.entity.Snowflake
import java.util.concurrent.TimeUnit

/**
 * Marks cooldowns of snowflakes for an action.
 * For example, response cooldowns for a guild for a user.
 */
class SnowflakeCooldown {
    // author, expiry timestamp
    private val cooldownExpiries: MutableMap<Long, Long> = mutableMapOf()

    /**
     * Try to initiate a new cooldown.
     * Will return false if they are already on cooldown.
     */
    fun tryCooldown(author: Snowflake): Boolean {
        return author.value.toLong().tryConsume().also {
            cleanup()
        }
    }

    /**
     * Try to consume an expiry.
     * If their expiry is in the future, return false.
     * Otherwise, add the new map entry and return true.
     */
    private fun Long.tryConsume(): Boolean {
        cooldownExpiries[this]?.let { expiry ->
            // they are still on cooldown
            if (expiry > System.currentTimeMillis()) {
                return false
            }
        }

        cooldownExpiries[this] = System.currentTimeMillis() + SNOWFLAKE_EXPIRY_MS
        return true
    }

    /**
     * Remove stale entries from the cooldowns.
     */
    private fun cleanup() {
        if (cooldownExpiries.isEmpty()) {
            return
        }

        val now = System.currentTimeMillis()
        cooldownExpiries.values.removeIf { future -> future < now }
    }

    private companion object {
        /***
         * How far in the future a cooldown will last.
         */
        val SNOWFLAKE_EXPIRY_MS: Long = TimeUnit.SECONDS.toMillis(10)
    }
}
