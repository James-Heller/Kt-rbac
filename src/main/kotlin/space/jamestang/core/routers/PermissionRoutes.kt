package space.jamestang.core.routers

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import space.jamestang.core.modules.Permission
import space.jamestang.core.modules.Permissions
import space.jamestang.core.plugins.TransactionException
import space.jamestang.core.util.Resp

fun Application.permissionRoutes() {

    routing {

        authenticate {
            get("/permission") {

                val currentPage = call.request.queryParameters["currentPage"]?.toInt() ?: 1
                val pageSize = call.request.queryParameters["pageSize"]?.toInt() ?: 10

                call.respond(Resp.data(Permissions.page(currentPage, pageSize)))
            }
            post("/permission") {

                val permission = call.receive<Permission>()

                if (Permissions.create(permission)) {
                    call.respond(Resp.success())
                } else {
                    call.respond(Resp.error("新增权限失败"))
                }
            }
            put("/permission") {

                val permission = call.receive<Permission>()

                if (Permissions.update(permission)) {
                    call.respond(Resp.success())
                } else {
                    call.respond(Resp.error("更新权限失败"))
                }
            }
            delete("/permission/{id}") {

                val id = call.parameters["id"]?.toInt() ?: throw TransactionException("ID不能为空")

                if (Permissions.logicDelete(id)) {
                    call.respond(Resp.success())
                } else {
                    call.respond(Resp.error("删除失败"))
                }
            }
        }
    }
}