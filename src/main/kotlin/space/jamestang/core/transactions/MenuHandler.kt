package space.jamestang.core.transactions

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import space.jamestang.core.modules.Menu
import space.jamestang.core.modules.Menus
import space.jamestang.core.plugins.TransactionException
import space.jamestang.core.util.Resp

object MenuHandler {

    suspend fun list(call: ApplicationCall){

        call.respond(Resp.data(Menus.getAll()))
    }


    suspend fun create(call: ApplicationCall){

        val menu = call.receive<Menu>()

        if (Menus.create(menu)){
            call.respond(Resp.success())
        }else{
            call.respond(Resp.error("新增菜单失败"))
        }
    }

    suspend fun update(call: ApplicationCall){

        val menu = call.receive<Menu>()

        if (Menus.update(menu)){
            call.respond(Resp.success())
        }else{
            call.respond(Resp.error("更新菜单失败"))
        }
    }


    suspend fun delete(call: ApplicationCall){

        val id = call.parameters["id"]?.toInt()?: throw TransactionException("ID不能为空")

        if (Menus.logicDelete(id)){
            call.respond(Resp.success())
        }else{
            call.respond(Resp.error("删除失败"))
        }
    }


}