plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("plugin.serialization") version Versions.KOTLIN_SERIALIZATION
    kotlin("jvm") version Versions.KOTLIN
}

group = "net.cakeyfox"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Ktor
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${Versions.KTOR}")
    implementation("io.ktor:ktor-server-netty:${Versions.KTOR}")
    implementation("io.ktor:ktor-server-core:${Versions.KTOR}")
    implementation("io.ktor:ktor-server-content-negotiation:${Versions.KTOR}")
    implementation("io.ktor:ktor-server-auth:${Versions.KTOR}")

    // Logging
    implementation("ch.qos.logback:logback-classic:${Versions.LOGBACK}")
    implementation("io.github.microutils:kotlin-logging:${Versions.KOTLIN_LOGGING}")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLIN_SERIALIZATION}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon:${Versions.KOTLIN_SERIALIZATION}")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    shadowJar {
        archiveBaseName.set("MarisaStuff")
        archiveVersion.set(version.toString())
        archiveClassifier.set("")

        manifest {
            attributes["Main-Class"] = "net.cakeyfox.marisastuff.MarisaStuffServerLauncher"
        }
    }
}

kotlin {
    jvmToolchain(Versions.JVM_TARGET)
}