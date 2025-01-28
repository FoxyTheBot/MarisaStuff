package net.cakeyfox.marisastuff

import kotlinx.coroutines.runBlocking
import kotlinx.io.IOException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import net.cakeyfox.marisastuff.config.ServerConfig
import net.cakeyfox.marisastuff.utils.HoconUtils.decodeFromString
import java.io.File
import kotlin.system.exitProcess

object MarisaStuffServerLauncher {
    @OptIn(ExperimentalSerializationApi::class)
    val hocon = Hocon { useArrayPolymorphism = true }

    @JvmStatic
    fun main(args: Array<String>) {
        val configFile = File(System.getProperty("conf") ?: "server.conf")

        if (!configFile.exists()) {
            println("Config file not found, creating default config file")

            copyFromJar("/server.conf", "./server.conf")
            return
        }

        val config =  readConfigFile<ServerConfig>(configFile)
        runBlocking {
            MarisaStuffServerInstance(config)
        }
    }

    private fun copyFromJar(input: String, output: String) {
        val inputStream = this::class.java.getResourceAsStream(input) ?: return
        File(output).writeBytes(inputStream.readAllBytes())
    }

    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T> readConfigFile(file: File): T {
        try {
            val json = file.readText()
            return hocon.decodeFromString<T>(json)
        } catch (e: IOException) {
            e.printStackTrace()
            exitProcess(1)
        }
    }
}