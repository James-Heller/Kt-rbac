package space.jamestang.core.modules

import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import org.ktorm.entity.Entity
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.varchar
import space.jamestang.core.expand.ktorm.EnhanceEntity
import space.jamestang.core.expand.ktorm.EnhanceTable
import space.jamestang.core.plugins.DB

interface Role : EnhanceEntity<Role> {

    companion object : Entity.Factory<Role>()

    var name: String
    var code: String

}

object Roles : EnhanceTable<Role>("rbac_role") {

    val name = varchar("name").bindTo { it.name }
    val code = varchar("code").bindTo { it.code }

    override val sequence: EntitySequence<Role, EnhanceTable<Role>> get() = DB.mysql.sequenceOf(this)

    fun updateRolePermissions(roleId: Int, permissionIds: List<Int>): Boolean{

        return DB.mysql.update(RolePermissions){
            set(RolePermissions.permissions, permissionIds)
            where { RolePermissions.roleId eq roleId }
        } == 1
    }

    fun updateRoleMenus(roleId: Int, menuIds: List<Int>): Boolean{

        return DB.mysql.update(RoleMenus){
            set(RoleMenus.menuId, menuIds)
            where { RoleMenus.roleId eq roleId }
        } == 1
    }
}