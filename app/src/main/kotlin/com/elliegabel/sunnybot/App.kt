package com.elliegabel.sunnybot

import com.elliegabel.sunnybot.config.AppConfig
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent

@OptIn(PrivilegedIntent::class)
suspend fun main() {
    AppConfig().setup().login {
        intents += Intent.MessageContent
        intents += Intent.GuildMembers
    }
}
