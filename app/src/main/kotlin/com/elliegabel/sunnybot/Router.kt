package com.elliegabel.sunnybot

import co.touchlab.kermit.Logger
import com.elliegabel.sunnybot.domain.AppFeature
import dev.kord.core.Kord
import org.koin.mp.KoinPlatform

class Router {
    fun register(kord: Kord) {
        Logger.withTag("bootstrap").i { "Routing..." }

        // Register features
        val features: List<AppFeature> = KoinPlatform.getKoin().getAll()
        features.forEach { it.register(kord) }
        Logger.withTag("bootstrap").i { "${features.size} features registered" }

        // Register commands
    }

}