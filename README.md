# SunnyBot

Just a goofy Discord bot intended for the community of Sunny :)
[Join the Discord](https://discord.com/invite/sunny)

## Biography

SunnyBot holds a large cultural significance.
She was first created on 23. June 2017,
before being left in the trash for several years,
and then freshly built up from the dust on 17. May 2024.
Only to be inevitably be forgotten about.

## Features

*Not much at all*

- Respond to basic regex matches with predefined messages.
- Not get exploited by raids, hopefully.
- Wave to people who write an introduction.
- Welcome messages.
- Stop unauthorized invite links.

### Planned Features

- f1 race tracker? or however it works
- birthday wishes
- polls
- dice roll
- trivia

## Nerd stuff

### Config

Bot config files are by default located at `/env/sunnybot`

### Building + Deploy

1. `./gradlew build`
2. Execute the docker-compose file.

### Composition

#### AppFeature

AppFeatures do stuff and can manifest as:
- Event Handlers
- Slash Commands (`Command` subtype)
- all of the above.

An `AppFeature` can have `FeatureProperties` which allow each Guild to modify how it behaves.
These are properties are stored in a `GuildSettings`

#### GuildSettings

*As of now these have to be manually defined in a JSON file (`guild-settings.json`),
if it is not defined the bot will not serve the Guild.
This bot is only meant for 1 server anyway.*

`GuildSettings` hold `FeatureProperties` which features can optionally define by:
1. Have a class implementing `FeatureProperties`
2. Mark as `@Serializable` with `@SerialName("<feature name>")`
3. Append to the features `SerializersModule`

Example:

`DumbResponder.kt`
```kotlin
class DumbResponder {
    @Serializable
    @SerialName("DumbResponder")
    data class Properties(
        val enabled: Boolean = false,
        val enabledChannels: Set<Long> = setOf(),
    ) : FeatureProperties
}
```

`SerializersModule.kt`
```kotlin
val featurePropertiesModule = 
    ...
            polymorphic(
                FeatureProperties::class,
                DumbResponder.Properties::class,
                DumbResponder.Properties.serializer(),
            )
    ...
```

This can then be accessed by:
```kotlin
        val server = settingsService.getGuildSettings(event.guildId)
        val properties = server.featureProperties<Properties>()
```
yipee.