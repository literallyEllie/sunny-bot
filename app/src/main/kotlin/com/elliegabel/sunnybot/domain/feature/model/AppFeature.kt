package com.elliegabel.sunnybot.domain.feature.model

import dev.kord.core.Kord

/**
 * Generic app feature which does something user-facing.
 */
interface AppFeature {
    /**
     * Register the feature with Kord.
     */
    suspend fun register(kord: Kord)
}

/**
 * Properties of a feature which can be configured by a Guild
 */
interface FeatureProperties
