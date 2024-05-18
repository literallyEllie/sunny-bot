package com.elliegabel.sunnybot.config

import com.elliegabel.sunnybot.domain.FeatureProperties
import com.elliegabel.sunnybot.domain.feature.DumbResponseFeature
import com.elliegabel.sunnybot.domain.feature.WelcomeFeature
import kotlinx.serialization.modules.SerializersModule

object SerializersModule {
    /**
     * Holds all feature properties
     */
    val featurePropertiesModule =
        SerializersModule {
            polymorphic(
                FeatureProperties::class,
                WelcomeFeature.Properties::class,
                WelcomeFeature.Properties.serializer()
            )
            polymorphic(
                FeatureProperties::class,
                DumbResponseFeature.Properties::class,
                DumbResponseFeature.Properties.serializer()
            )
        }
}
