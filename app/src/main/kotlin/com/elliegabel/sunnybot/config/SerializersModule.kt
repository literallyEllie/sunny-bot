package com.elliegabel.sunnybot.config

import com.elliegabel.sunnybot.domain.feature.DumbResponseFeature
import com.elliegabel.sunnybot.domain.feature.ModerationFeature
import com.elliegabel.sunnybot.domain.feature.NoInvitesFeature
import com.elliegabel.sunnybot.domain.feature.WelcomeFeature
import com.elliegabel.sunnybot.domain.feature.model.FeatureProperties
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
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
                WelcomeFeature.Properties.serializer(),
            )
            polymorphic(
                FeatureProperties::class,
                DumbResponseFeature.Properties::class,
                DumbResponseFeature.Properties.serializer(),
            )
            // moderation
            polymorphic(
                FeatureProperties::class,
                ModerationFeature.Properties::class,
                ModerationFeature.Properties.serializer(),
            )
            polymorphic(
                FeatureProperties::class,
                NoInvitesFeature.Properties::class,
                NoInvitesFeature.Properties.serializer(),
            )
        }

    object RegexSerializer : KSerializer<Regex> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Regex", PrimitiveKind.STRING)

        override fun serialize(
            encoder: Encoder,
            value: Regex,
        ) {
            encoder.encodeString(value.pattern)
        }

        override fun deserialize(decoder: Decoder): Regex {
            return Regex(decoder.decodeString())
        }
    }
}
