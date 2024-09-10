package space.jamestang.core.routers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import space.jamestang.core.transactions.RoleHandler

fun Application.roleRoutes() {

     routing {
         authenticate {
             get("/role/page") { RoleHandler.page(call) }
             post("/role/create") { RoleHandler.create(call) }
             put("/role/update") { RoleHandler.update(call) }
             delete("/role/{id}") { RoleHandler.delete(call) }
         }
     }
}