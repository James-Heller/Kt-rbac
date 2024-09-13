package space.jamestang.core.routers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.request.receive
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import space.jamestang.core.modules.User
import space.jamestang.core.modules.Users
import space.jamestang.core.plugins.TransactionException
import space.jamestang.core.util.Resp

fun Application.authenticationRoutes() {

    routing {
        post("/auth/login") {

            val formData = call.receiveParameters()
            val username = formData["username"]!!
            val password = formData["password"]!!

            Users.login(username, password)?.let {
                call.respond(Resp.data("Bearer $it"))
            } ?: call.respond(Resp.error("login failed"))
        }
        post("/auth/register") {

            val user = call.receive<User>()

            if (Users.register(user)) {
                call.respond(Resp.success())
            } else {
                call.respond(Resp.error("register failed"))
            }
        }


        authenticate {
            get("/auth/info") {

                val principal = call.principal<JWTPrincipal>()
                val user = Users.selectById(principal!!.payload.getClaim("id").asInt())

                call.respond(Resp.data(user!!))
            }
            get("/auth/menus") {
                val currentUserId = call.principal<JWTPrincipal>()!!.payload.getClaim("id").asInt()
                val data = Users.getUserMenu(currentUserId)
                call.respond(Resp.data(data))
            }
            get("/auth/permissions") {

                val currentUserId = call.principal<JWTPrincipal>()!!.getClaim("id", Int::class)!!

                val data = Users.getUserPermissions(currentUserId)

                call.respond(Resp.data(data))
            }
            post("auth/changePass") {

                val currentUserId = call.principal<JWTPrincipal>()!!.getClaim("id", Int::class)!!
                val newPassword = call.receive<String>()

                if (Users.changePassword(currentUserId, newPassword)) {
                    call.respond(Resp.success())
                } else {
                    call.respond(Resp.error("change password failed"))
                }
            }
            post("/auth/updateUserRole/{id}") {

                val userId = call.parameters["id"] ?: throw TransactionException("id is required")

                val roles = call.receive<List<Int>>()

                if (Users.updateRole(userId.toInt(), roles)) {
                    call.respond(Resp.success())
                } else {
                    call.respond(Resp.error("update role failed"))
                }
            }
            delete("/auth/disable/{id}") {

                val id = call.parameters["id"]?.toInt() ?: throw TransactionException("id is required")

                if (Users.disable(id)) {
                    call.respond(Resp.success())
                } else {
                    call.respond(Resp.error("disable failed"))
                }
            }
        }
    }
}