package space.jamestang.core.modules

import org.ktorm.entity.Entity
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.sequenceOf
import org.ktorm.jackson.json
import org.ktorm.schema.varchar
import space.jamestang.core.expand.ktorm.EnhanceEntity
import space.jamestang.core.expand.ktorm.EnhanceTable
import space.jamestang.core.plugins.DB

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
}