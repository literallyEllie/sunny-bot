package com.elliegabel.sunnybot.config

import com.elliegabel.sunnybot.domain.feature.DumbResponseFeature
import com.elliegabel.sunnybot.domain.feature.NoInvitesFeature
import com.elliegabel.sunnybot.domain.feature.WelcomeFeature
import com.elliegabel.sunnybot.domain.feature.command.Command8Ball
import com.elliegabel.sunnybot.domain.feature.command.CommandCoinFlip
import com.elliegabel.sunnybot.domain.feature.model.AppFeature
import com.elliegabel.sunnybot.domain.feature.model.Command
import com.elliegabel.sunnybot.domain.repository.GuildSettingsRepository
import com.elliegabel.sunnybot.domain.service.DiscordService
import com.elliegabel.sunnybot.domain.service.GuildSettingsService
import org.koin.dsl.bind
import org.koin.dsl.module

object ModulesConfig {
    private val configModule =
        module {
            single { AppConfig() }
        }
    private val settingsModule =
        module {
            single { GuildSettingsRepository() }
            single { GuildSettingsService(get()) }
        }
    private val discordModule =
        module {
            single { DiscordService() }
        }
    private val featuresModule =
        module {
            single { DumbResponseFeature(get(), get()) } bind AppFeature::class
            single { WelcomeFeature(get(), get()) } bind AppFeature::class
            single { NoInvitesFeature(get(), get()) } bind AppFeature::class
        }
    private val commandsModule =
        module {
            single { Command8Ball() } bind Command::class bind AppFeature::class
            single { CommandCoinFlip() } bind Command::class bind AppFeature::class
        }

    internal val allModules =
        listOf(
            configModule,
            settingsModule,
            discordModule,
            featuresModule,
            commandsModule,
        )
}
