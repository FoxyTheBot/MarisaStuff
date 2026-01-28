package net.cakeyfox.marisastuff

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.fromFilePath
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.deflate
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.compression.minimumSize
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondFile
import io.ktor.server.routing.*
import net.cakeyfox.marisastuff.config.ServerConfig
import net.coobird.thumbnailator.Thumbnails
import java.io.ByteArrayOutputStream
import java.io.File

class MarisaStuffServerInstance(
    config: ServerConfig
) {
    companion object {
        private const val STUFF_FOLDER = "C:\\Users\\WinG4merBR\\Downloads"
    }

    private val server = embeddedServer(Netty, port = config.port) {
        install(ContentNegotiation) {
            json()
        }

        install(Compression) {
            gzip { priority = 1.0 }
            deflate { priority = 10.0; minimumSize(1024) }
        }

        routing {
            get("/{path...}") {
                val path = call.parameters.getAll("path")?.joinToString("/") ?: return@get
                val file = File(STUFF_FOLDER, path)
                val size = call.request.queryParameters["size"]?.toIntOrNull()


                if (!file.exists() || file.isDirectory) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                val extension = file.extension.lowercase()
                val isImage = extension in listOf("jpg", "jpeg", "png", "webp")

                if (isImage && size != null) {
                    if (size > 2048) return@get call.respond(HttpStatusCode.BadRequest)

                    val outputStream = ByteArrayOutputStream()

                    try {
                        Thumbnails.of(file)
                            .size(size, size)
                            .outputFormat(if (extension == "webp") "png" else extension)
                            .toOutputStream(outputStream)

                        val contentType = ContentType.fromFilePath(file.path).first()
                        call.respondBytes(outputStream.toByteArray(), contentType)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, "Erro ao processar imagem")
                    }
                } else {
                    call.respondFile(file)
                }
            }
        }
    }

    init {
        server.start(wait = true)
    }
}