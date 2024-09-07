package space.jamestang.core.expand.ktor

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import space.jamestang.core.util.*

inline fun Route.postWithPermission(url: String, permission: String, crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit) {
    post(url){

        if (call.principal<JWTPrincipal>()!!.subject!! == "JamesTang"){
            body(Unit)
        }else{
            call.respond(Resp.error(403, "Permission Denied"))
        }

    }

}

fun Route.getWithPermission(url: String, permission: String, body: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit) {
    get(url){

        val subject = call.principal<JWTPrincipal>()?.subject

        if (subject == "JamesTang"){
            body(Unit)
        }else{
            call.respond(Resp.error(403, "Permission Denied"))
        }

    }

}