package space.jamestang.core.transactions

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.ktorm.dsl.eq
import space.jamestang.core.Config
import space.jamestang.core.modules.User
import space.jamestang.core.modules.Users
import space.jamestang.core.plugins.TransactionException
import space.jamestang.core.util.Resp
import java.util.*

object AuthenticationHandler {

    suspend fun login(call: ApplicationCall) {
        val formData = call.receiveParameters()
        val username = formData["username"]
        val password = formData["password"]
        if (username == null || password == null){
            call.respond(Resp.error("用户名或密码为空"))
            return
        }

        val token = login(username, password)
        if (token != null){
            call.respond(Resp.data("Bearer $token"))
        }else{
            call.respond(Resp.error("用户名或密码错误"))
        }
    }

    suspend fun register(call: ApplicationCall){

        val user = call.receive<User>()

        user.password = encodePassword(user.password)

        if (register(user)){
            call.respond(Resp.success())
        }else{
            call.respond(Resp.error("register failed"))
        }
    }

    suspend fun disable(call: ApplicationCall){

        val id = call.parameters["id"]?.toInt() ?: throw TransactionException("id is null")
        if (Users.logicDelete(id)){
            call.respond(Resp.success())
        }else{
            call.respond(Resp.error("disable failed"))
        }
    }


    suspend fun info(call: ApplicationCall) {
        val currentUserId = call.principal<JWTPrincipal>()!!.payload.getClaim("id")
        val user = Users.selectUniqueByColumn(Users.id, currentUserId.asInt())
        if (user != null){
            call.respond(Resp.data(user))
        }else{
            call.respond(Resp.error("user not found"))
        }
    }

    suspend fun changeUserPassword(call: ApplicationCall) {

        val currentUserId = call.principal<JWTPrincipal>()!!.payload.getClaim("id")
        val newPassword = call.receive<String>()

        if (changePassword(currentUserId.asInt(), newPassword)) {
            call.respond(Resp.success())
        }else{
            call.respond(Resp.error("change password failed"))
        }
    }




    private fun changePassword(userId: Int, newPassword: String) = Users.update(Users.password, encodePassword(newPassword)){
        Users.id eq userId
    }

    private fun register(user: User): Boolean {

        user.password = encodePassword(user.password)



        return Users.create(user)
    }

    private fun login(username: String, password: String): String?{

        val user = Users.selectUniqueByColumn(Users.username, username)
        if (user != null){
            return if (verifyPassword(password, user.password)){
                issueToken(user.username, user.id!!)
            }else{
                null
            }
        }

        return null
    }

    private fun issueToken(name: String, userId: Int): String {
        val builder = JWT.create()

        builder.withIssuer(Config.jwt_domain)
            .withAudience(Config.jwt_audience)
            .withSubject(name)
            .withClaim("id", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))

        return builder.sign(Algorithm.HMAC256(Config.jwt_secret))
    }

    private fun encodePassword(rawCharSequence: String): String {

        return BCrypt.with(BCrypt.Version.VERSION_2B).hashToString(10, rawCharSequence.toCharArray())
    }

    private fun verifyPassword(rawCharSequence: String, hashedPassword: String): Boolean {
        try {
            BCrypt.verifyer(BCrypt.Version.VERSION_2B).verify(rawCharSequence.toCharArray(), hashedPassword).verified
        }catch (e: Exception){
            return false
        }
        return true
    }
}