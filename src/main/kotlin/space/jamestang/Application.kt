package space.jamestang

import io.ktor.server.application.*
import space.jamestang.core.*
import space.jamestang.core.plugins.*
import space.jamestang.core.routers.authenticationRoutes
import space.jamestang.core.routers.menuRoutes
import space.jamestang.core.routers.permissionRoutes
import space.jamestang.core.routers.roleRoutes
import space.jamestang.core.routers.userRoutes

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    initConfigurations()
    configureExceptionHandler()
    configureSecurity()
    configureSerialization()
    DB.initDatabase()
    Redis.initialize()

    authenticationRoutes()
    menuRoutes()
    userRoutes()
    roleRoutes()
    permissionRoutes()
}
