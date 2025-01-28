package net.cakeyfox.marisastuff

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import net.cakeyfox.marisastuff.config.ServerConfig
import java.io.File

class MarisaStuffServerInstance(
    config: ServerConfig
) {
    companion object {
        private const val STUFF_FOLDER = "/opt/marisa-stuff"
    }

    private val server = embeddedServer(Netty, port = config.port) {
        install(ContentNegotiation) {
            json()
        }

        routing {
            staticFiles("/", File(STUFF_FOLDER))
        }
    }

    init {
        server.start(wait = true)
    }
}