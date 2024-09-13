package space.jamestang.core.modules

import org.ktorm.entity.Entity
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.varchar
import space.jamestang.core.expand.ktorm.EnhanceEntity
import space.jamestang.core.expand.ktorm.EnhanceTable
import space.jamestang.core.plugins.DB

interface Permission : EnhanceEntity<Permission> {

    companion object : Entity.Factory<Permission>()

    var code: String
    var nameZh: String

}

object Permissions : EnhanceTable<Permission>("rbac_permission") {

    val code = varchar("code").bindTo { it.code }
    val nameZh = varchar("name_zh").bindTo { it.nameZh }

    override val sequence: EntitySequence<Permission, EnhanceTable<Permission>> get() = DB.mysql.sequenceOf(this)

}