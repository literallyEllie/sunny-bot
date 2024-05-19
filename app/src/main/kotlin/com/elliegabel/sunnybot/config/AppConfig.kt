package com.elliegabel.sunnybot.config

import co.touchlab.kermit.Logger
import com.elliegabel.sunnybot.domain.service.DiscordService
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.environmentProperties
import org.koin.fileProperties

class AppConfig : KoinComponent {
    suspend fun setup() {
        Logger.withTag("bootstrap").i { "Hello! Configuring bot..." }
        GlobalContext.startKoin {
            modules(ModulesConfig.allModules)

            // prioritise env
            fileProperties()
            environmentProperties()
        }

        Logger.withTag("bootstrap").i { "App configured!" }

        val discordService = getKoin().get<DiscordService>()
        discordService.initialise() // blocks the thread
    }
}
