package com.elliegabel.sunnybot.domain.feature.model

import dev.kord.core.entity.interaction.InteractionCommand

/**
 * Represents a feature whose primary function is to run a command.
 */
interface Command : AppFeature {
    val name: String
    val description: String

    fun shouldHandle(command: InteractionCommand): Boolean = command.rootName == name
}
