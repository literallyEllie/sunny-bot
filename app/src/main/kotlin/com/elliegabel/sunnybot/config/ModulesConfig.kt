package com.elliegabel.sunnybot.config

import com.elliegabel.sunnybot.Router
import com.elliegabel.sunnybot.domain.AppFeature
import com.elliegabel.sunnybot.domain.feature.DumbResponseFeature
import com.elliegabel.sunnybot.domain.feature.WelcomeFeature
import com.elliegabel.sunnybot.domain.service.DiscordService
import org.koin.dsl.bind
import org.koin.dsl.module

object ModulesConfig {
    private val configModule =
        module {
            single { AppConfig() }
            single { Router() }
        }
    private val discordModule =
        module {
            single { DiscordService() }
        }

    private val featuresModule =
        module {
            single { DumbResponseFeature(get()) } bind AppFeature::class
            single { WelcomeFeature(get()) } bind AppFeature::class
        }

    internal val allModules =
        listOf(
            configModule,
            discordModule,
            featuresModule,
        )
}
