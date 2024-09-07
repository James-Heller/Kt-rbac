package space.jamestang

import io.ktor.server.application.*
import space.jamestang.core.*
import space.jamestang.core.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    initConfigurations()
    configureExceptionHandler()
    configureSecurity()
    configureSerialization()
    configureRouting()
    DB.initDatabase()
    Redis.initialize()
}
