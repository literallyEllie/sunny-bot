package com.elliegabel.sunnybot.domain.feature.model

import dev.kord.core.Kord

interface AppFeature {
    suspend fun register(kord: Kord)
}

interface FeatureProperties
