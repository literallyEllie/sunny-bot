package com.elliegabel.sunnybot.domain

import co.touchlab.kermit.Logger
import com.elliegabel.sunnybot.domain.feature.model.FeatureProperties
import kotlinx.serialization.Serializable

@Serializable
data class GuildSettings(val id: Long, val features: Set<FeatureProperties>) {
    internal val logger: Logger
        get() = Logger.withTag(id.toString())

    inline fun <reified T : FeatureProperties> featureProperties(): T? {
        return features.filterIsInstance<T>().firstOrNull()
    }
}
