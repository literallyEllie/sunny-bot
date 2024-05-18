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

### Planned Features

- f1 race tracker? or however it works

## Nerd stuff

### Basic flow

1. The Discord bot is instantiated using the [Kord](https://github.com/kordlib/kord) library in the "AppConfig".
2. Gather all the "AppFeature"s with [Koin](https://insert-koin.io) and pass the Kord instance.
3. "App" logs in with the required intents.

### The bot

The bits that make up the bot are referred to as "AppFeature"s.
They are registered in the Router.

They can either be event handlers, slash commands or a combination.

AppFeatures can have properties which are associated to a Server.