package space.jamestang.core.transactions

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import space.jamestang.core.modules.Role
import space.jamestang.core.modules.Roles
import space.jamestang.core.plugins.TransactionException
import space.jamestang.core.util.Resp

object RoleHandler {

    suspend fun page(call: ApplicationCall) {

        val page = call.request.queryParameters["page"]?.toInt() ?: throw TransactionException("页码不能为空")
        val size = call.request.queryParameters["size"]?.toInt() ?: throw TransactionException("每页数量不能为空")

        call.respond(Resp.data(Roles.page(page, size)))
    }

    suspend fun create(call: ApplicationCall) {

        val role = call.receive<Role>()

        Roles.create(role)

        call.respond(Resp.success())
    }

    suspend fun update(call: ApplicationCall) {

        val role = call.receive<Role>()

        Roles.update(role)

        call.respond(Resp.success())
    }

    suspend fun delete(call: ApplicationCall) {

        val id = call.parameters["id"]?.toInt() ?: throw TransactionException("ID不能为空")

        Roles.logicDelete(id)

        call.respond(Resp.success())
    }
}