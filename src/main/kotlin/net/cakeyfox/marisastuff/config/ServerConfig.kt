package net.cakeyfox.marisastuff.config

import kotlinx.serialization.Serializable

@Serializable
data class ServerConfig(
    val port: Int,
    val stuffFolder: String
)
