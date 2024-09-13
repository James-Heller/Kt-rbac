package space.jamestang.core.routers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import space.jamestang.core.modules.Menu
import space.jamestang.core.modules.Menus
import space.jamestang.core.plugins.TransactionException
import space.jamestang.core.util.Resp

fun Application.menuRoutes(){

    routing {
        authenticate {
            get("/menu"){
                call.respond(Resp.data(Menus.getAll()))
            }
            post("/menu"){
                val menu = call.receive<Menu>()

                if (Menus.create(menu)){
                    call.respond(Resp.success())
                }else{
                    call.respond(Resp.error("新增菜单失败"))
                }
            }
            put("/menu"){
                val menu = call.receive<Menu>()

                if (Menus.update(menu)){
                    call.respond(Resp.success())
                }else{
                    call.respond(Resp.error("更新菜单失败"))
                }
            }
            delete("/menu/{id}"){
                val id = call.parameters["id"]?.toInt()?: throw TransactionException("ID不能为空")

                if (Menus.logicDelete(id)){
                    call.respond(Resp.success())
                }else{
                    call.respond(Resp.error("删除失败"))
                }
            }
        }
    }
}