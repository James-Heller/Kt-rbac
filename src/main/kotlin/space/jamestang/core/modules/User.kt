package space.jamestang.core.modules

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.inList
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.dsl.update
import org.ktorm.dsl.where
import org.ktorm.entity.Entity
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.sequenceOf
import org.ktorm.jackson.json
import org.ktorm.schema.varchar
import space.jamestang.core.Config
import space.jamestang.core.expand.ktorm.EnhanceEntity
import space.jamestang.core.expand.ktorm.EnhanceTable
import space.jamestang.core.plugins.DB
import java.util.Date

interface User: EnhanceEntity<User> {

    companion object: Entity.Factory<User>()

    var username: String
    var email: String
    var password: String
    var phone: String
    var nickname: String
    var avatar: String
    var roles: List<Int>
}

object Users: EnhanceTable<User>("rbac_users") {

    val username = varchar("username").bindTo { it.username }
    val nickname = varchar("nickname").bindTo { it.nickname }
    val email = varchar("email").bindTo { it.email }
    val password = varchar("password").bindTo { it.password }
    val phone = varchar("phone").bindTo { it.phone }
    val avatar = varchar("avatar").bindTo { it.avatar }
    val roles = json<List<Int>>("roles").bindTo { it.roles }

    override val sequence: EntitySequence<User, EnhanceTable<User>>
        get() = DB.mysql.sequenceOf(this)


    fun changePassword(id: Int, password: String): Boolean {
        return DB.mysql.update(Users){
            set(Users.password, encodePassword(password))
            where { Users.id eq id }
        } == 1
    }

    fun updateRole(id: Int, roles: List<Int>): Boolean {
        return DB.mysql.update(Users){
            set(Users.roles, roles)
            where { Users.id eq id }
        } == 1
    }

    fun register(user: User): Boolean {
        user.password = encodePassword(user.password)
        return create(user)
    }

    fun login(username: String, password: String): String?{

        val user = selectUniqueByColumn(Users.username, username)
        if (user != null){
            return if (verifyPassword(password, user.password)){
                issueToken(user.username, user.id!!)
            }else{
                null
            }
        }

        return null
    }

    fun getUserMenu(id: Int): List<Menu>{
        val user = selectById(id)
        val roles = user!!.roles

        val data = DB.mysql.from(Menus)
            .leftJoin(RoleMenus, Menus.id eq RoleMenus.menuId)
            .select(Menus.columns)
            .where((RoleMenus.roleId inList roles) and (Menus.deleted eq false))
            .map(Menus::createEntity)

        return data
    }

    fun getUserPermissions(id: Int): List<Permission>{

        val userRole = selectById(id)?.roles ?: emptyList()

        val permissionList = DB.mysql.from(Permissions).leftJoin(RolePermissions, Roles.id eq RolePermissions.roleId)
            .leftJoin(Users, Users.id eq id).select(Permissions.columns)
            .where(RolePermissions.roleId inList userRole).map(Permissions::createEntity)

        return permissionList
    }

    fun disable(id: Int): Boolean {

        return logicDelete(id)
    }

    private fun encodePassword(rawCharSequence: String): String = BCrypt.with(BCrypt.Version.VERSION_2B).hashToString(10, rawCharSequence.toCharArray())

    private fun issueToken(name: String, userId: Int): String {
        val builder = JWT.create()

        builder.withIssuer(Config.jwt_domain)
            .withAudience(Config.jwt_audience)
            .withSubject(name)
            .withClaim("id", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 360000))

        return builder.sign(Algorithm.HMAC256(Config.jwt_secret))
    }

    private fun verifyPassword(rawCharSequence: String, hashedPassword: String): Boolean {
        try {
            BCrypt.verifyer(BCrypt.Version.VERSION_2B).verify(rawCharSequence.toCharArray(), hashedPassword).verified
        }catch (_: Exception){
            return false
        }
        return true
    }


}