package space.jamestang.core.modules

import org.ktorm.entity.Entity
import org.ktorm.jackson.json
import org.ktorm.schema.Table
import org.ktorm.schema.int

interface RolePermission : Entity<RolePermission>{

    companion object : Entity.Factory<RolePermission>()

    var roleId: Int
    var permissions: List<Int>
}

object RolePermissions : Table<RolePermission>("rbac_role_permission"){

    val roleId = int("role_id").bindTo { it.roleId }
    val permissions = json<List<Int>>("permissions").bindTo { it.permissions }


}