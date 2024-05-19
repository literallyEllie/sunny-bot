package com.elliegabel.sunnybot.domain.service

import com.elliegabel.sunnybot.domain.GuildSettings
import com.elliegabel.sunnybot.domain.repository.GuildSettingsRepository
import dev.kord.common.entity.Snowflake

class GuildSettingsService(private val repository: GuildSettingsRepository) {
    fun getGuildSettings(guildId: Snowflake?): GuildSettings? {
        if (guildId == null) {
            return null
        }

        return repository[guildId.value.toLong()]
    }
}
