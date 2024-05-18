package com.elliegabel.sunnybot.ext

import dev.kord.core.entity.Message

fun Message.isBot(): Boolean {
    return author?.isBot != false
}
