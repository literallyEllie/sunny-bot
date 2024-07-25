package com.elliegabel.sunnybot.domain.repository

import co.touchlab.kermit.Logger
import com.elliegabel.sunnybot.config.SerializersModule
import com.elliegabel.sunnybot.domain.GuildSettings
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Individual Guild settings.
 * In the future will put it in an actual database or something.
 */
class GuildSettingsRepository {
    private val json =
        Json {
            serializersModule = SerializersModule.featurePropertiesModule
        }

    private val repository: Map<Long, GuildSettings> = loadSettings()

    /**
     * Get the settings of a guild.
     */
    fun getSettings(guildId: Long): GuildSettings? {
        return repository[guildId]
    }

    operator fun get(guildId: Long): GuildSettings? = getSettings(guildId)

    private fun loadSettings(): Map<Long, GuildSettings> {
        val settings = File(DIR_CONFIG, FILE_REPOSITORY)
        if (!settings.isFile) {
            Logger.w { "Guild settings repository does not exist: ${settings.name}" }
            return mapOf()
        }

        val decoded = json.decodeFromString<List<GuildSettings>>(settings.readText())
        Logger.withTag("bootstrap").i { "Loaded ${decoded.size} server definitions" }

        return decoded.associateBy { it.guildId }
    }

    private companion object {
        const val DIR_CONFIG = "config"
        const val FILE_REPOSITORY = "guild-settings.json"
    }
}
