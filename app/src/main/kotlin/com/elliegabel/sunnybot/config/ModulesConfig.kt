package com.elliegabel.sunnybot.config

import com.elliegabel.sunnybot.Router
import com.elliegabel.sunnybot.domain.AppFeature
import com.elliegabel.sunnybot.domain.DiscordService
import com.elliegabel.sunnybot.features.DumbResponder
import org.koin.dsl.module

object ModulesConfig {
    private val configModule = module {
        single { AppConfig() }
        single { Router() }
    }
    private val discordModule = module {
        single { DiscordService() }
    }

    private val featuresModule = module {
        single<AppFeature> { DumbResponder(get()) }
    }

    internal val allModules = listOf(
        configModule,
        discordModule,
        featuresModule
    )
}