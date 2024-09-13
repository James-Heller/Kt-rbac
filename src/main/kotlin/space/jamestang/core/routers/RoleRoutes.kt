package space.jamestang.core.routers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import space.jamestang.core.modules.Role
import space.jamestang.core.modules.Roles
import space.jamestang.core.plugins.TransactionException
import space.jamestang.core.util.Resp

fun Application.roleRoutes() {

    routing {
        authenticate {
            get("/role/page") {
                val page = call.request.queryParameters["page"]?.toInt() ?: throw TransactionException("页码不能为空")
                val size =
                    call.request.queryParameters["size"]?.toInt() ?: throw TransactionException("每页数量不能为空")

                call.respond(Resp.data(Roles.page(page, size)))
            }
            post("/role/create") {
                val role = call.receive<Role>()

                if (Roles.create(role)) {
                    call.respond(Resp.success())
                } else {
                    call.respond(Resp.error("角色名已存在"))
                }

            }
            put("/role/update") {
                val role = call.receive<Role>()

                Roles.update(role)

                call.respond(Resp.success())
            }
            delete("/role/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw TransactionException("ID不能为空")

                Roles.logicDelete(id)

                call.respond(Resp.success())
            }


            put("/role/updatePermission/{id}") {

                val id = call.parameters["id"]?.toInt() ?: throw TransactionException("ID不能为空")
                val permissionIds = call.receive<List<Int>>()

                if (Roles.updateRolePermissions(id, permissionIds)) {
                    call.respond(Resp.success())
                } else {
                    call.respond(Resp.error("更新失败"))
                }
            }

            put("/role/updateMenus/{id"){

                val id = call.parameters["id"]?.toInt() ?: throw TransactionException("ID不能为空")
                val menuIds = call.receive<List<Int>>()

                if (Roles.updateRoleMenus(id, menuIds)){
                    call.respond(Resp.success())
                }else {
                    call.respond(Resp.error("更新失败"))
                }
            }
        }
    }
}