package com.elliegabel.sunnybot.config

import co.touchlab.kermit.Logger
import com.elliegabel.sunnybot.Router
import dev.kord.core.Kord
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext
import org.koin.environmentProperties
import org.koin.fileProperties

class AppConfig : KoinComponent {
    private val router: Router by inject()

    suspend fun setup(): Kord {
        Logger.withTag("bootstrap").i { "Hello! Configuring bot..." }
        GlobalContext.startKoin {
            modules(ModulesConfig.allModules)

            // priortise env
            fileProperties()
            environmentProperties()
        }

        // setup Kord
        val kord = Kord(getKoin().getProperty("bot_token") ?: throw NullPointerException("Bot token required"))
        router.register(kord)

        Logger.withTag("bootstrap").i { "App configured and ready for login!" }
        return kord
    }
}