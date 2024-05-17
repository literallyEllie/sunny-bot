package com.elliegabel.sunnybot.domain

import dev.kord.core.Kord

interface AppFeature {
    fun register(kord: Kord)
}