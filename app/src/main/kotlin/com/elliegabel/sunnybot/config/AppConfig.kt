package com.elliegabel.sunnybot.config

import co.touchlab.kermit.Logger
import com.elliegabel.sunnybot.domain.feature.model.AppFeature
import dev.kord.core.Kord
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.environmentProperties
import org.koin.fileProperties
import org.koin.mp.KoinPlatform

class AppConfig : KoinComponent {
    suspend fun setup(): Kord {
        Logger.withTag("bootstrap").i { "Hello! SunnyBot v${System.getenv("VERSION") ?: "undefined"} starting..." }
        GlobalContext.startKoin {
            modules(ModulesConfig.allModules)

            // prioritise env
            fileProperties()
            environmentProperties()
        }

        val kord =
            Kord(
                KoinPlatform.getKoin().getProperty("DISCORD_TOKEN")
                    ?: throw NullPointerException("Bot token required"),
            )

        // Init features
        val features: List<AppFeature> = KoinPlatform.getKoin().getAll()
        Logger.withTag("bootstrap").i { "Discovered ${features.size} features, registering" }
        features.forEach { it.register(kord) }

        Logger.withTag("bootstrap").i { "App configured, ready to log in!" }
        return kord
    }
}
