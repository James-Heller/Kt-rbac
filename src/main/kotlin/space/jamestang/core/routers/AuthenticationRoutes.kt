package space.jamestang.core.routers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import space.jamestang.core.transactions.AuthenticationHandler

fun Application.authenticationRoutes(){

    routing {
        post("/auth/login") { AuthenticationHandler.login(call) }
        post("/auth/register") { AuthenticationHandler.register(call) }
        delete("/auth/disable/{id}") { AuthenticationHandler.disable(call) }

        authenticate {
            get("/auth/info") { AuthenticationHandler.info(call) }
        }
    }
}