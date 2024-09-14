package space.jamestang.core.routers

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import space.jamestang.core.modules.User
import space.jamestang.core.modules.Users
import space.jamestang.core.util.Resp

fun Application.userRoutes() {


    routing {
        authenticate{
            get("/user") {
                val currentPage = call.request.queryParameters["currentPage"]?.toInt() ?: 1
                val pageSize = call.request.queryParameters["pageSize"]?.toInt() ?: 10

                val data = Users.page(currentPage, pageSize)

                call.respond(Resp.data(data))
            }

            post<User>("/user") {

                val user = call.receive<User>()

                if (Users.create(user)) {
                    call.respond(Resp.success())
                } else {
                    call.respond(Resp.error("insert failed"))
                }
            }

            put("/user") {
                val user = call.receive<User>()

                if (Users.update(user)) {
                    call.respond(Resp.success())
                } else {
                    call.respond(Resp.error("update failed"))
                }
            }

            delete("/user/{id}") {
                val id = call.parameters["id"]?.toInt() ?: 0

                if (Users.delete(id)) {
                    call.respond(Resp.success())
                } else {
                    call.respond(Resp.error("delete failed"))
                }
            }
        }
    }
}