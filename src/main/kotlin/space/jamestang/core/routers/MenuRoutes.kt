package space.jamestang.core.routers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import space.jamestang.core.transactions.MenuHandler

fun Application.menuRoutes(){

    routing {
        authenticate {
            get("/menu"){ MenuHandler.list(call) }
            post("/menu"){ MenuHandler.create(call) }
            put("/menu"){ MenuHandler.update(call) }
            delete("/menu/{id}"){ MenuHandler.delete(call) }
        }
    }
}